package model

import exception.MemberIsInactiveException
import helper.BaseSpec
import org.joda.time.DateTime
import repository.MemberRepository

import scala.util.Random

/**
  * * # Created by wacharint on 8/6/2016 AD.
  **/
class MemberDetailModelSpec extends BaseSpec
{
    """When open the member detail model""" should
        {
            """display only inactive/edit button""" in
                {
                    // <editor-fold desc="Setup">

                    val member = generateTestActiveMember

                    // </editor-fold>

                    // <editor-fold desc="Execute">

                    val testModel = new MemberDetailModel(member.memberId)
                    // </editor-fold>

                    // <editor-fold desc="Verify">

                    testModel.clearButtonVisible mustEqual false
                    testModel.saveButtonVisible mustEqual false
                    testModel.editButtonVisible mustEqual true
                    testModel.inActiveButtonVisible mustEqual true


                    // </editor-fold>
            }
        }

    """Add point function""" should
    {
        """add point to the active member""" in
        {
            // <editor-fold desc="Setup">

            val member = generateTestActiveMember
            val testModel = new MemberDetailModel(member.memberId)
            val currentPoint = member.point

            // </editor-fold>

            // <editor-fold desc="Execute">

            testModel.addPoint(10)

            // </editor-fold>

            // <editor-fold desc="Verify">

            testModel.point() mustEqual currentPoint + 10
            val dbPoint = new MemberRepository().get(Seq("member_id" -> testModel.member.memberId.toString)).head.asInstanceOf[MemberRepository].point
            dbPoint mustEqual currentPoint + 10

            // </editor-fold>
        }

        """not add point to the inactive member""" in
        {
            // <editor-fold desc="Setup">

            val member = generateTestInactiveMember
            val testModel = new MemberDetailModel(member.memberId)
            val currentPoint = member.point

            // </editor-fold>

            // <editor-fold desc="Execute">

            testModel.addPoint(10) must throwA[MemberIsInactiveException]

            // </editor-fold>

            // <editor-fold desc="Verify">

            testModel.point() mustEqual currentPoint
            val dbPoint = new MemberRepository().get(Seq("member_id" -> testModel.member.memberId.toString)).head.asInstanceOf[MemberRepository].point
            dbPoint mustEqual currentPoint

            // </editor-fold>
        }
    }

    def generateTestActiveMember =
    {
        val ret = new MemberRepository()
        {
            firstName = "ffafds"
            lastName = "werfg"
            id = new Random().nextInt()
            tel = "45678+"
            address = "oiuytrd"
            sex = "M"
            birth = DateTime.now().minusYears(10)
            point = 567l
        }

        ret.memberId = ret.insert()
        ret
    }

    def generateTestInactiveMember =
    {
        val ret = new MemberRepository()
        {
            firstName = "ffafds"
            lastName = "werfg"
            id = new Random().nextInt()
            tel = "45678+"
            address = "oiuytrd"
            sex = "M"
            birth = DateTime.now().minusYears(10)
            point = 567l
            recStatus = 0
        }

        ret.memberId = ret.insert()
        ret
    }

    override protected def beforeAll(): Unit =
    {}

    override protected def afterAll(): Unit =
    {}
}
