package model

import context.CoreContext
import exception.MemberIsInactiveException
import repository.MemberRepository
import rx.Obs

/**
  * * # Created by wacharint on 8/1/2016 AD.
  **/
class MemberDetailModel(memberId: Int)(implicit context: CoreContext) extends IMemberInfoModel
{
    member = member.get(Seq("member_id" -> memberId.toString)).asInstanceOf[Seq[MemberRepository]].head
    firstName.update(member.firstName)
    lastName.update(member.lastName)
    id.update(member.id)
    tel.update(member.tel)
    address.update(member.address)
    birth.update(member.birth)
    sex.update(member.sex)
    point.update(member.point)
    isActive.update(member.recStatus.equals(1))

    // <editor-fold desc="obs">

    val pointObs = Obs(point, "onPointChange")(() =>
    {
        //
    })

    //</editor-fold>

    // <editor-fold desc="def">

    def addPoint(newPoint: Int) =
    {
        if( member.recStatus.equals(0) )
        {
            throw new MemberIsInactiveException("The member_id: %s is inactive".format(member.memberId))
        } else
        {
            point.update(point() + newPoint)
            member.point = point()
            member.update(Seq("member_id"))
        }
    }
    // </editor-fold>

    override val saveButtonVisible: Boolean = false
    override val editButtonVisible: Boolean = true
    override val inActiveButtonVisible: Boolean = true
    override val clearButtonVisible: Boolean = false
    override val addPointButtonVisible: Boolean = true
    override val usePointButtonVisible: Boolean = true
}
