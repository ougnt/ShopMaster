package model

import context.CoreContext
import repository.MemberRepository

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

    override val saveButtonVisible: Boolean = false
    override val editButtonVisible: Boolean = true
    override val inActiveButtonVisible: Boolean = true
    override val clearButtonVisible: Boolean = false
}
