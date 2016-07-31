package repository

import java.lang.reflect.Field

import context.CoreContext
import org.joda.time.DateTime

/**
  * * # Created by wacharint on 7/27/2016 AD.
  **/
class MemberRepository(implicit context: CoreContext) extends InjectAble{

    var memberId = 0
    var firstName = ""
    var lastName = ""
    var id = 0l
    var tel = ""
    var address = ""
    var sex = ""
    var birth = DateTime.now()
    var point = 0l

    recStatus = 1

    override val callContext: CoreContext = context
    override val tableName: String = "member"
    override var fields: Seq[Field] = classOf[MemberRepository].getDeclaredFields
}
