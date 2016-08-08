package repository

import java.sql.SQLException

import helper.BaseSpec
import org.specs2.matcher.Matcher

import scala.util.Random

/**
  * * # Created by wacharint on 8/6/2016 AD.
  **/
class RepositorySpecs extends BaseSpec
{
    """PointActivityRepository""" should
        {
            """be able to add "add" activity to the existing member""" in
            {

                    // <editor-fold desc="Setup">

                    val member = new MemberRepository()
                    member.memberId = member.insert()

                    // </editor-fold>

                    // <editor-fold desc="Execute">

                    val activity = new PointActivityHistoryRepository()
                    {
                        memberId = member.memberId
                        activityType = "A"
                        point = 19
                    }
                    activity.historyId = activity.insert

                    // </editor-fold>

                    // <editor-fold desc="Verify">

                    val inDb = new PointActivityHistoryRepository().get(Seq("history_id" -> activity.historyId.toString)).head.asInstanceOf[PointActivityHistoryRepository]
                    inDb must beSamePointActivity(activity)

                    // </editor-fold>
                }

            """throw member is not exist when "add" to the not existing member """ in
            {

                // <editor-fold desc="Setup">


                // </editor-fold>

                // <editor-fold desc="Execute">

                val activity = new PointActivityHistoryRepository()
                {
                    memberId = new Random().nextInt()
                    activityType = "A"
                    point = 0
                }

                // </editor-fold>

                // <editor-fold desc="Verify">

                activity.insert must throwA[SQLException]

                // </editor-fold>
    }

            """throw member is not exist when action is not allow """ in
            {

                // <editor-fold desc="Setup">


                // </editor-fold>

                // <editor-fold desc="Execute">

                val activity = new PointActivityHistoryRepository()
                {
                    memberId = new Random().nextInt()
                    activityType = "Z"
                    point = 0
                }

                // </editor-fold>

                // <editor-fold desc="Verify">

                activity.insert must throwA[SQLException]

                // </editor-fold>
            }

            """be able to add "redeem" activity to the existing member""" in
            {

                // <editor-fold desc="Setup">

                val member = new MemberRepository()
                member.memberId = member.insert()

                // </editor-fold>

                // <editor-fold desc="Execute">

                val activity = new PointActivityHistoryRepository()
                {
                    memberId = member.memberId
                    activityType = "R"
                    point = -19
                }
                activity.historyId = activity.insert

                // </editor-fold>

                // <editor-fold desc="Verify">

                val inDb = new PointActivityHistoryRepository().get(Seq("history_id" -> activity.historyId.toString)).head.asInstanceOf[PointActivityHistoryRepository]
                inDb must beSamePointActivity(activity)

                // </editor-fold>
        }

            """be able to add "update" activity to the existing member""" in
            {

                // <editor-fold desc="Setup">

                val member = new MemberRepository()
                member.memberId = member.insert()

                // </editor-fold>

                // <editor-fold desc="Execute">

                val activity = new PointActivityHistoryRepository()
                {
                    memberId = member.memberId
                    activityType = "U"
                    point = 100
                }
                activity.historyId = activity.insert

                // </editor-fold>

                // <editor-fold desc="Verify">

                val inDb = new PointActivityHistoryRepository().get(Seq("history_id" -> activity.historyId.toString)).head.asInstanceOf[PointActivityHistoryRepository]
                inDb must beSamePointActivity(activity)

                // </editor-fold>
            }
        }


    // <editor-fold desc="def">

    def beSamePointActivity(exp: PointActivityHistoryRepository): Matcher[PointActivityHistoryRepository] =
        (act: PointActivityHistoryRepository) =>
            (
                exp.historyId == act.historyId &&
                    exp.memberId == act.memberId &&
                    exp.activityType == act.activityType &&
                    exp.point == act.point,
                "",
                ""
                )

    // </editor-fold>

    override protected def beforeAll(): Unit =
    {}

    override protected def afterAll(): Unit =
    {}
}
