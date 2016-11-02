package model

import javafx.event.{ActionEvent, EventHandler}

import context.CoreContext
import repository.PointRedeemProfileRepository
import rx.Var

import scalafx.scene.control.{ButtonType, Alert}

/**
  * * # Created by wacharint on 8/11/2016 AD.
  **/
class PointRedeemOptionSettingModel(implicit context: CoreContext)
{
    val redemptionProfile = new PointRedeemProfileRepository().get(Seq("point_redeem_profile_id" -> "1")).head.asInstanceOf[PointRedeemProfileRepository]

    def commit(): EventHandler[ActionEvent] = {
        new EventHandler[ActionEvent] {
            override def handle(event: ActionEvent): Unit = {
                redemptionProfile.discountPerInterval = discountPerInterval()
                redemptionProfile.minimumRedemption = minimumRedemption()
                redemptionProfile.pointInterval = redemptionInterval()
                redemptionProfile.update(Seq("point_redeem_profile_id"))

                new Alert(Alert.AlertType.Information, "Saved", ButtonType.OK).showAndWait()
            }
        }
    }

    val minimumRedemption = Var(redemptionProfile.minimumRedemption)

    val discountPerInterval = Var(redemptionProfile.discountPerInterval)

    val redemptionInterval = Var(redemptionProfile.pointInterval)

    val cancelButtonText = "Cancel"

    val commitButtonText = "Commit"

    val minimumRedemptionText = "Minimum Point Redemption"

    val discountPerIntervalText = "Discount Per Redemption Interval"

    val redemptionIntervalText = "Point Redemption Interval"

    var isDirty = Var(false)
}
