package model

import context.CoreContext
import repository.PointRedeemOptionRepository
import rx.{Rx, Var}

/**
  * * # Created by wacharint on 8/11/2016 AD.
  **/
class PointRedeemOptionSettingModel(implicit context: CoreContext)
{
    val currentSettings = new PointRedeemOptionRepository().get(Seq("rec_status" -> "1")).asInstanceOf[Seq[PointRedeemOptionRepository]]
    var newSettings: Seq[PointRedeemOptionRepository] = currentSettings

    val discountColumnText = "Discount"
    val pointColumnText = "Points"
    val updateButtonText = "Update"
    val cancelChangeButtonText = "Cancel all changes"
    val commitButtonText = "Commit"
    val reverseButtonText = "Reverse changes"

    var isDirty = Var(false)
    var updateButtonEnable = Rx(isDirty)

    def updateAllSettings() =
    {
        if(newSettings != Nil)
        {
            removeCurrentSetting()
            addNewSettings()
        }
    }

    private def addNewSettings() = if(newSettings != Nil) currentSettings.foreach(_.insert())

    private def removeCurrentSetting() = currentSettings.foreach(s =>
    {
        s.recStatus = 0
        s.update(Seq("redeem_option_id"))
    })

}
