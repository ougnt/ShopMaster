package repository

import java.lang.reflect.Field

import context.CoreContext

/**
  * * # Created by wacharint on 8/6/2016 AD.
  **/
class PointActivityHistoryRepository(implicit context: CoreContext) extends InjectAble
{
    var historyId = 0
    var memberId = 0
    var activityType = "A"
    var point = 0

    recStatus = 1

    override val callContext: CoreContext = context
    override val tableName: String = "point_history"
    override var fields: Seq[Field] = classOf[PointActivityHistoryRepository].getDeclaredFields
}
