package model

import context.CoreContext

/**
  * * # Created by wacharint on 7/25/2016 AD.
  **/
class RegistrationModel(implicit context: CoreContext) extends IMemberInfoModel
{

    override def save() =
    {
        member.firstName = firstName()
        member.lastName = lastName()
        member.id = id()
        member.tel = tel()
        member.address = address()
        member.sex = sex()
        member.birth = birth()

        member.insert()
    }

    override val saveButtonVisible: Boolean = true
    override val editButtonVisible: Boolean = false
    override val inActiveButtonVisible: Boolean = false
    override val clearButtonVisible: Boolean = true
    override val addPointButtonVisible: Boolean = false
    override val usePointButtonVisible: Boolean = false
    override val pointHistoryButtonVisible: Boolean = false
}
