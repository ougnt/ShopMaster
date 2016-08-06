package model

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

    def generateTestInactiveMember = new MemberRepository()
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
