package scene

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.input.{KeyEvent, MouseEvent}

import context.CoreContext
import model.PointRedeemOptionSettingModel
import repository.PointRedeemOptionRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafx.scene.layout.{FlowPane, BorderPane}

/**
  * * # Created by wacharint on 8/11/2016 AD.
  **/
class PointRedeemOptionSettingPane(implicit context: CoreContext) extends BorderPane
{
    id = "point-redeem-option-setting-pane"
    var dataModel = new PointRedeemOptionSettingModel()
    val optionTable = generateOptionTable()
    center = optionTable
    bottom = generateButtonFlowPane()

    // <editor-fold desc="def">

    def generateOptionTable(): TableView[PointRedeemOptionRepository] =
    {
        val settings = ObservableBuffer(dataModel.currentSettings)

        val pointCol = new TableColumn[PointRedeemOptionRepository, Int](dataModel.pointColumnText)
        {
            cellValueFactory = cdf => ObjectProperty(cdf.value.point)
            cellFactory = _ => generateTextFieldTableCell()
        }
        val discountCol = new TableColumn[PointRedeemOptionRepository, Int](dataModel.discountColumnText)
        {
            cellValueFactory = cdf => ObjectProperty(cdf.value.discount)
            cellFactory = _ => generateTextFieldTableCell()
        }

        val table = new TableView[PointRedeemOptionRepository](settings)
        {

        }
        table.editable = true

        Future
        {
            while (table.width.value == 0)
            {
                Thread.sleep(10)
            }
            pointCol.prefWidth = table.width.value * 0.499
            discountCol.prefWidth = table.width.value * 0.499
            table.columns ++= List(pointCol, discountCol)
        }

        table
    }

    def generateButtonFlowPane(): FlowPane =
    {
        val commitButton = new Button()
        {
            text = dataModel.commitButtonText
            onAction = new EventHandler[ActionEvent] {
                override def handle(event: ActionEvent): Unit =
                {
                    commitTableInfo()
                }
            }
        }

        val reverseButton = new Button()
        {
            text = dataModel.reverseButtonText
            onAction = new EventHandler[ActionEvent] {
                override def handle(event: ActionEvent): Unit =
                {
                    dataModel = new PointRedeemOptionSettingModel()
                    center = generateOptionTable()
                }
            }
        }

        val pane = new FlowPane
        {
            id = "point-redeem-option-setting-model-flow-pane"
            children = List(commitButton, reverseButton)
        }

        pane
    }

    def commitTableInfo(): Unit =
    {
        var newSettings: Seq[PointRedeemOptionRepository] = Nil
        for (i <- 0 until optionTable.items.getValue.size())
        {
            newSettings = newSettings ++ Seq(optionTable.items.getValue.get(i))
        }
        dataModel.newSettings = newSettings
        dataModel.newSettings.foreach(s =>
        {
            s.update(Seq("redeem_option_id"))
        })
    }

    def generateTextFieldTableCell(): TableCell[PointRedeemOptionRepository, Int] =
    {
        new TableCell[PointRedeemOptionRepository, Int]
        {
            item.onChange
            {
                (obj, oldValue, newValue) =>
                {
                    val copiedNewValue = newValue
                    graphic = new TextField
                    {
                        text = newValue.toString.replaceAll("[^0-9]", "")
                        text.onChange
                        {
                            text = text.value.replaceAll("[^0-9]", "")
                            item.update(text.value.toInt)
                            optionTable.items.getValue.get(tableRow.value.getIndex).point = item.value
                        }
                        onMouseClicked = new EventHandler[MouseEvent]
                        {
                            override def handle(event: MouseEvent): Unit =
                            {
                                tableView.value.getSelectionModel.clearSelection()
                                tableRow.value.updateSelected(true)
                            }
                        }
                        onKeyPressed = new EventHandler[KeyEvent]
                        {
                            override def handle(event: KeyEvent): Unit =
                            {
                                tableView.value.getSelectionModel.clearSelection()
                                tableRow.value.updateSelected(true)
                            }
                        }
                    }

                }
            }
        }
    }

    // </editor-fold>
}
