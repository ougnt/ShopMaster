package model

import context.CoreContext
import org.joda.time.DateTime
import repository.MemberRepository
import rx.{Obs, Rx, Var}

/**
  * * # Created by wacharint on 7/27/2016 AD.
  **/
abstract class IMemberInfoModel(implicit context: CoreContext)
{
    var member = new MemberRepository

    val headerLabel = "Member Registration"
    val firstNameLabel = "First Name"
    val lastNameLabel = "Last Name"
    val idLabel = "ID"
    val telLabel = "Tel"
    val addressLabel = "Address"
    val sexLabel = "Sex"
    val birthLabel = "Birth Date"
    val saveButtonText = "Save"
    val clearButtonText = "Clear"
    val editButtonText = "Edit"
    val inActiveButtonText = "Inactive this member"
    val activeButtonText = "Active this member"
    val pointLabel = "Points"
    val addPointButtonText = "Add point"
    val redeemPointButtonText = "Redeem point"
    val pointHistoryButtonText = "Points History"
    val saveButtonVisible: Boolean
    val clearButtonVisible: Boolean
    val editButtonVisible: Boolean
    val inActiveButtonVisible: Boolean
    val addPointButtonVisible: Boolean
    val usePointButtonVisible: Boolean
    val pointHistoryButtonVisible: Boolean

    var firstName = Var(member.firstName)
    var lastName = Var(member.lastName)
    var id = Var(member.id)
    var tel = Var(member.tel)
    var address = Var(member.address)
    var sex = Var(member.sex)
    var birth = Var(member.birth)
    var point = Var(member.point)
    var isActive = Var(member.recStatus.equals(1))

    //<editor-fold desc="Rx">

    val age = Rx
    {
        (DateTime.now.getMillis - birth().getMillis) / 1000 / 60 / 60 / 24 / 365
    }

    //</editor-fold>

    //<editor-fold desc="callback">

    new Obs(isActive, onInactiveStatusChange)

    //</editor-fold>

    def save(): Unit = ???

    def edit(): String = ???

    def toggleActiveStatus(): Unit =
    {
        isActive.update(!isActive())
        member.recStatus = if(isActive()) 1 else 0
        member.insertOrUpdate(Seq("member_id" -> member.memberId.toString))
    }

    def onInactiveStatusChange(): Unit =
    {
//        member.recStatus = if(isActive()) 1 else 0
    }
}
