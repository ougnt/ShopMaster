package model

import context.CoreContext
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import repository.MemberRepository
import rx.{Obs, Var}
import scene.MemberSearchPane


/**
  * * # Created by wacharint on 7/28/2016 AD.
  **/
class MemberSearchModel(scene: MemberSearchPane)(implicit context: CoreContext)
{
    //<editor-fold desc="variable and value">

    var members: Seq[MemberRepository] = Seq()
    val searchKeyword: Var[String] = Var("")

    // constant text
    val nameColumnText = "Name"
    val idColumnText = "ID"
    val telColumnText = "Tel"
    val keywordLabel = "Enter search criteria here"
    val searchButton = "Search"

    private val searchObs = new Obs(searchKeyword, onSearchChange)
    private var lastAction = DateTime.now

    //</editor-fold>

    //<editor-fold desc="methods">

    def onSearchChange(): Unit =
    {

        if (searchKeyword().length == 0)
        {
            // do nothing
        } else
        {
            lastAction = DateTime.now()
            val tempMember = new MemberRepository()
            val searchKey = "%" + searchKeyword() + "%"
            var searchDate = ""
            try
            {
                searchDate = DateTimeFormat.forPattern("ddMMyyyy").parseDateTime(searchKeyword()).minusYears(543).toString(
                    "yyyy-MM-dd")
                searchDate = "%"+searchDate+"%"
            } catch
            {
                case e: IllegalArgumentException => searchDate = "xxx"
            }
            val sql =
                """
                        SELECT   *
                        FROM     %s
                        WHERE    id like '$#SEARCH_KEY#$'
                        OR       first_name like '$#SEARCH_KEY#$'
                        OR       last_name like '$#SEARCH_KEY#$'
                        OR       tel like '$#SEARCH_KEY#$'
                        OR       birth like '$#SEARCH_DATE#$'

                """.stripMargin.format(tempMember.tableName + "_vu").replaceAll(
                    """\$\#SEARCH_KEY\#\$""",
                    searchKey).replaceAll("""\$\#SEARCH_DATE\#\$""", searchDate)

            members = tempMember.get(sql).asInstanceOf[Seq[MemberRepository]].distinct
            lastAction = DateTime.now()
            val pane = scene.generateMainPane
            scene.center = pane
        }
    }

    //</editor-fold>
}
