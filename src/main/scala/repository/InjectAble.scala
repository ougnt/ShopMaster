package repository

import java.lang.reflect.Field
import java.sql.{ResultSet, SQLException, Statement}
import java.util.UUID

import context.CoreContext
import org.joda.time.DateTime


/**
  * * # Created by wacharint on 7/6/15.
  **/

trait InjectAble
{

    val callContext: CoreContext
    var fields: Seq[Field]
    val tableName: String

    // database columns
    var recCreatedWhen: DateTime = DateTime.now()
    var recCreatedBy: UUID = null
    var recModifiedWhen: DateTime = null
    var recModifiedBy: UUID = null
    var recStatus: Int = 0

    def insert(): Int =
    {

        if (callContext.connection.isEmpty)
        {

            callContext.connect()
        }

        var sqlStatement =
            """INSERT INTO %s
              |(#columns#)
              |VALUES
              |(#values#)""".stripMargin.format(tableName)

        fields = fields.filterNot(field => field.getName == "connection").
            filterNot(field => field.getName == "databaseUrl").
            filterNot(field => field.getName == "serialVersionUID").
            filterNot(field => field.getName == "callContext").
            filterNot(field => field.getName == "fields").
            filterNot(field => field.getName == "tableName").
            filterNot(field => field.getName == "recCreatedWhen").
            filterNot(field => field.getName == "recCreatedBy").
            filterNot(field => field.getName == "recModifiedWhen").
            filterNot(field => field.getName == "recModifiedBy")

        fields.foreach(field =>
        {

            field.setAccessible(true)
            val value = field.get(this)
            if (value != null)
            {

                sqlStatement = sqlStatement.replace(
                    """#columns#""",
                    field.getName.replaceAll( """([A-Z])""", """_$1""").toLowerCase.concat( """,#columns#"""))
                sqlStatement = sqlStatement.replace( """#values#""", """N'%s',#values#""".format(value.toString))
            }
        })

        sqlStatement = sqlStatement.replace( """#columns#""", """rec_created_by,#columns#""")
        sqlStatement = sqlStatement.replace(
            """#values#""",
            """N'%s',#values#""".format(callContext.currentUserId.toString))
        sqlStatement = sqlStatement.replace( """#columns#""", """rec_created_when,#columns#""")
        sqlStatement = sqlStatement.replace( """#values#""", """N'%s',#values#""".format(DateTime.now().toString()))

        sqlStatement = sqlStatement.replace(""",#columns#""", "")
        sqlStatement = sqlStatement.replace(""",#values#""", "")

        try
        {
            val conn = callContext.connection.get
            //      val statement = conn.createStatement()
            val statement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)
            val res = statement.executeUpdate()
            val resset = statement.getGeneratedKeys

            resset.next
            if (resset.getRow == 0)
                0
            else
            {
                resset.getInt(1)
            }
        } catch
        {
            case ex: SQLException =>
            {
                Console.println(ex.getMessage)
                throw ex
            }
            case ex: Exception =>
            {
                Console.println(ex.getMessage)
                throw ex
            }
        }
    }

    def get(customSql: String): Seq[InjectAble] =
    {

        if (callContext.connection.isEmpty)
        {

            callContext.connect()
        }

        var res: ResultSet = null

        try
        {
            if (callContext.connection.isEmpty)
            {

                callContext.connect()
            }
            val conn = callContext.connection.get
            val statement = conn.createStatement()
            res = statement.executeQuery(customSql)
        } catch
        {
            case e: SQLException => throw e
            case e: Exception => throw e
        }
        fromResultSet(res)
    }

    def fromResultSet(res: ResultSet): Seq[InjectAble] =
    {

        var hasData = false
        var returnedSeq: Seq[InjectAble] = Nil

        while (res.next)
        {

            fields = fields.filterNot(field => field.getName == "connection").
                filterNot(field => field.getName == "databaseUrl").
                filterNot(field => field.getName == "serialVersionUID").
                filterNot(field => field.getName == "callContext").
                filterNot(field => field.getName == "fields").
                filterNot(field => field.getName == "tableName")

            val result: InjectAble = this.getClass.getConstructor(classOf[CoreContext]).newInstance(callContext)

            fields.foreach(field =>
            {

                field.setAccessible(true)
                val columnName = field.getName.replaceAll("""([A-Z])""", """_$1""").toLowerCase
                val value = res.getObject(columnName)
                if (value != null)
                {

                    hasData = true

                    field.getType.getSimpleName match
                    {
                        case "UUID" =>

                            val typeValue = UUID.fromString(value.asInstanceOf[String])
                            field.set(result, typeValue)

                        case "DateTime" =>

                            val stringVal = value.asInstanceOf[String]
                            val typeValue = DateTime.parse(stringVal)
                            field.set(result, typeValue)

                        case _ =>

                            field.set(result, value)
                    }
                } else
                {

                    field.set(result, null)
                }
            })

            returnedSeq = returnedSeq ++ Seq(result)
        }
        if (hasData) returnedSeq else Nil
    }

    def get(keyValues: Seq[(String, String)]): Seq[InjectAble] =
    {

        if (callContext.connection.isEmpty)
        {

            callContext.connect()
        }

        var sqlStatement = """SELECT * FROM %s_vu WHERE #columns# = N'#values#'""".format(tableName)

        keyValues.foreach(kv =>
            sqlStatement = sqlStatement.replace("#columns#", kv._1).replace("#values#", kv._2).concat(
                " AND #columns# = N'#values#'")
        )

        sqlStatement = sqlStatement.replace("""WHERE #columns# = N'#values#'""", "")
        sqlStatement = sqlStatement.replace("""AND #columns# = N'#values#'""", "")

        var hasData = false
        var returnedSeq: Seq[InjectAble] = Nil

        var res: ResultSet = null

        try
        {
            if (callContext.connection.isEmpty)
            {

                callContext.connect()
            }
            val conn = callContext.connection.get
            val statement = conn.createStatement()
            res = statement.executeQuery(sqlStatement)
        } catch
        {
            case e: SQLException => throw e
            case e: Exception => throw e
        }

        fromResultSet(res)
    }

    def insertOrUpdate(keyValues: Seq[(String, String)]) =
    {

        if (callContext.connection.isEmpty)
        {

            callContext.connect()
        }

        try
        {
            insert()
        } catch
        {

            case e: SQLException =>
            {
                var sqlStatement =
                    """UPDATE %s
                      |SET #columns# = '#values#',""".stripMargin.format(tableName)

                fields = fields.filterNot(field => field.getName == "connection").
                    filterNot(field => field.getName == "databaseUrl").
                    filterNot(field => field.getName == "serialVersionUID").
                    filterNot(field => field.getName == "callContext").
                    filterNot(field => field.getName == "fields").
                    filterNot(field => field.getName == "tableName").
                    filterNot(field => field.getName == "recCreatedWhen").
                    filterNot(field => field.getName == "recCreatedBy").
                    filterNot(field => field.getName == "recModifiedWhen").
                    filterNot(field => field.getName == "recModifiedBy")

                fields.foreach(field =>
                {

                    field.setAccessible(true)
                    val value = field.get(this)
                    if (value != null)
                    {

                        sqlStatement = sqlStatement.replace(
                            """#columns#""",
                            field.getName.replaceAll( """([A-Z])""", """_$1"""))
                        sqlStatement = sqlStatement.replace( """#values#""", value.toString).concat(
                            """#columns# = N'#values#',""")
                    }
                })

                sqlStatement = sqlStatement.replace( """#columns#""", "rec_modified_by")
                sqlStatement = sqlStatement.replace( """#values#""", callContext.currentUserId.toString).concat(
                    """#columns# = N'#values#'""")
                sqlStatement = sqlStatement.replace( """#columns#""", "rec_modified_when")
                sqlStatement = sqlStatement.replace( """#values#""", DateTime.now().toString()).concat(
                    """ WHERE #columns# = N'#values#'""")

                keyValues.foreach(kv =>
                    sqlStatement = sqlStatement.replace("#columns#", kv._1).replace("#values#", kv._2).concat(
                        """AND #columns# = N'#values#'""")
                )

                sqlStatement = sqlStatement.replace(""" WHERE #columns# = N'#values#'""", "")
                sqlStatement = sqlStatement.replace("""AND #columns# = N'#values#'""", "")

                try
                {
                    val conn = callContext.connection.get
                    val statement = conn.createStatement()
                    val res = statement.executeUpdate(sqlStatement)
                } catch
                {
                    case ex: SQLException =>
                    {
                        throw ex
                    }
                    case ex: Exception =>
                    {
                        throw ex
                    }
                }
            }
        }
    }

    def update(keyValues: Seq[String]) =
    {

        if (callContext.connection.isEmpty)
        {

            callContext.connect()
        }

        var sqlStatement =
            """UPDATE %s
              |SET #columns# = '#values#',""".stripMargin.format(tableName)

        fields = fields.filterNot(field => field.getName == "connection").
            filterNot(field => field.getName == "databaseUrl").
            filterNot(field => field.getName == "serialVersionUID").
            filterNot(field => field.getName == "callContext").
            filterNot(field => field.getName == "fields").
            filterNot(field => field.getName == "tableName").
            filterNot(field => field.getName == "recCreatedWhen").
            filterNot(field => field.getName == "recCreatedBy").
            filterNot(field => field.getName == "recModifiedWhen").
            filterNot(field => field.getName == "recModifiedBy")

        fields.foreach(field =>
        {

            field.setAccessible(true)
            val value = field.get(this)
            if (value != null)
            {

                sqlStatement = sqlStatement.replace(
                    """#columns#""",
                    field.getName.replaceAll( """([A-Z])""", """_$1"""))
                sqlStatement = sqlStatement.replace( """#values#""", value.toString).concat(
                    """#columns# = N'#values#',""")
            }
        })

        sqlStatement = sqlStatement.replace( """#columns#""", "rec_modified_by")
        sqlStatement = sqlStatement.replace( """#values#""", callContext.currentUserId.toString).concat(
            """#columns# = N'#values#'""")
        sqlStatement = sqlStatement.replace( """#columns#""", "rec_modified_when")
        sqlStatement = sqlStatement.replace( """#values#""", DateTime.now().toString()).concat(
            """ WHERE #columns# = N'#values#'""")

        keyValues.foreach(kv =>
            {
                sqlStatement = sqlStatement.replace("#columns#", kv).replace("#values#",
                    fields.find(_.getName.replaceAll(
                        """([A-Z])""",
                        """_$1""").equalsIgnoreCase(kv)).get.get(this).toString).concat(
                    """AND #columns# = N'#values#'""")
            }
        )

        sqlStatement = sqlStatement.replace(""" WHERE #columns# = N'#values#'""", "")
        sqlStatement = sqlStatement.replace("""AND #columns# = N'#values#'""", "")

        try
        {
            val conn = callContext.connection.get
            val statement = conn.createStatement()
            val res = statement.executeUpdate(sqlStatement)
        } catch
        {
            case ex: SQLException =>
            {
                throw ex
            }
            case ex: Exception =>
            {
                throw ex
            }
        }
    }
}

