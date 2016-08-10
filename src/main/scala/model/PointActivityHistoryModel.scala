package model

import context.CoreContext
import repository.{PointActivityHistoryRepository, MemberRepository}

/**
  * * # Created by wacharint on 8/10/2016 AD.
  **/
class PointActivityHistoryModel(val member: MemberRepository)(implicit context: CoreContext)
{
    val histories = new PointActivityHistoryRepository().get(Seq("member_id" -> member.memberId.toString)).asInstanceOf[Seq[PointActivityHistoryRepository]]
    val activityTypeColumnText = "Activity Type"
    val pointColText = "Points"
    val dateColText = "Transaction Date"
}
