package scene

import javafx.scene.layout.ColumnConstraints

import context.CoreContext
import model.PointRedeemOptionSettingModel

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, FlowPane, GridPane}

/**
  * * # Created by wacharint on 8/11/2016 AD.
  **/
class PointRedeemOptionSettingPane(implicit context: CoreContext) extends BorderPane {
    id = "point-redeem-option-setting-pane"
    var dataModel = new PointRedeemOptionSettingModel()
    var commitButton: Button = null
    center = generateRedemptionProfile()

    bottom = generateButtonFlowPane()

    // <editor-fold desc="def">

    def generateRedemptionProfile(): GridPane = {

        val intervalLabel = new Label(){
            text = dataModel.redemptionIntervalText
        }

        val discountPerIntervalLabel = new Label() {
            text = dataModel.discountPerIntervalText
        }

        val minimumRedemptionLabel = new Label() {
            text = dataModel.minimumRedemptionText
        }

        var labels = List(intervalLabel, discountPerIntervalLabel, minimumRedemptionLabel)

        val intervalTextField = new TextField() {
            text = dataModel.redemptionInterval() toString()
            text.onChange {
                text = text.value.replaceAll("[^\\d]", "")
                dataModel.redemptionInterval.update(text.value.toInt)
            }
        }
        val minimumRedemptionTextField = new TextField() {
            text = dataModel.minimumRedemption() toString()
            text.onChange {
                text = text.value.replaceAll("[^\\d]", "")
                dataModel.minimumRedemption.update(text.value.toInt)
            }
        }
        val discountPerIntervalTextField = new TextField() {
            text = dataModel.discountPerInterval() toString()
            text.onChange {
                text = text.value.replaceAll("[^\\d]", "")
                dataModel.discountPerInterval.update(text.value.toInt)
            }
        }

        val textFields = List(intervalTextField, discountPerIntervalTextField, minimumRedemptionTextField)

        val intervalPane = new FlowPane()
        intervalPane.children = List(intervalLabel, intervalTextField)

        val discountPane = new FlowPane()
        discountPane.children = List(discountPerIntervalLabel, discountPerIntervalTextField)

        val minimumRedemptionPane = new FlowPane()
        minimumRedemptionPane.children = List(minimumRedemptionLabel, minimumRedemptionTextField)

        val pane = new GridPane()

        val col1 = new ColumnConstraints()
        col1.setPercentWidth(100)
        pane.columnConstraints.add(col1)
        pane.addRow(0, intervalPane)
        pane.addRow(1, discountPane)
        pane.addRow(2, minimumRedemptionPane)

        var maxLengthLabel = 0d

        Future {
            while(labels(0).width.value == 0) {
                Thread.sleep(10)
            }
            labels.foreach(l =>
                maxLengthLabel = if(l.width.apply() > maxLengthLabel)
                    l.width.value
                else
                    maxLengthLabel
            )
            intervalPane.hgap = maxLengthLabel + 10 - intervalPane.children.get(0).asInstanceOf[javafx.scene.control.Label].getWidth
            discountPane.hgap = maxLengthLabel + 10 - discountPane.children.get(0).asInstanceOf[javafx.scene.control.Label].getWidth
            minimumRedemptionPane.hgap = maxLengthLabel + 10 - minimumRedemptionPane.children.get(0).asInstanceOf[javafx.scene.control.Label].getWidth
        }

        pane
    }

    def generateButtonFlowPane(): FlowPane = {

        val commitButton = new Button() {
            text = dataModel.commitButtonText
            onAction = {
                dataModel.commit()
            }
        }

        val pane = new FlowPane()
        pane.id = "button-control-flow-pane"
        pane.children = List(commitButton)
        pane
    }

    // </editor-fold>
}
