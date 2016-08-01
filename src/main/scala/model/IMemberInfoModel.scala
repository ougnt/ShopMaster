package model

import context.CoreContext
import org.joda.time.DateTime
import repository.MemberRepository
import rx.{Rx, Var}

/**
  * * # Created by wacharint on 7/27/2016 AD.
  **/
abstract class IMemberInfoModel(implicit context: CoreContext)
{
    val member = new MemberRepository

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
    val inActiveButtonText = "Inactive"
    val pointLabel = "Points"
    val saveButtonVisible: Boolean
    val clearButtonVisible: Boolean
    val editButtonVisible: Boolean
    val inActiveButtonVisible: Boolean

    var firstName = Var("")
    var lastName = Var("")
    var id = Var(0l)
    var tel = Var("")
    var address = Var("")
    var sex = Var("")
    var birth = Var(DateTime.now)
    var point = Var(0l)

    //<editor-fold desc="Rx">

    val age = Rx
    {
        (DateTime.now.getMillis - birth().getMillis) / 1000 / 60 / 60 / 24 / 365
    }

    //</editor-fold>

    def save(): Unit = ???

    def edit(): Unit = ???

    def inactive(): Unit = ???
}
