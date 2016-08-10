package scene

import context.CoreContext
import model.PointActivityHistoryModel
import repository.{PointActivityHistoryRepository, MemberRepository}

import scala.concurrent.Future
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafx.scene.layout.BorderPane

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * * # Created by wacharint on 8/10/2016 AD.
  **/
class PointActivityHistoryPane(val member: MemberRepository)(implicit context: CoreContext) extends BorderPane
{
    val dataModel = new PointActivityHistoryModel(member)
    val historyTable = generateHistoryTable()
    center = historyTable
    id = "point-activity-history-pane"

    def generateHistoryTable(): TableView[PointActivityHistoryRepository] =
    {
        // TODO : add top pane to fill in the member name and last name

        val historyBuffer = ObservableBuffer(dataModel.histories.sortBy(_.recCreatedWhen.toString()).reverse)

        val activityTypeCol = new TableColumn[PointActivityHistoryRepository, String](dataModel.activityTypeColumnText)
        {
            cellValueFactory = cdf => StringProperty(cdf.value.activityType match {
                case "A" => "Add"
                case "U" => "Update"
                case "R" => "Redeem"
                case _ => cdf.value.activityType
            })
        }

        val pointCol = new TableColumn[PointActivityHistoryRepository, String](dataModel.pointColText)
        {
            cellValueFactory = cdf => StringProperty(cdf.value.point.toString)
        }

        val dateCol = new TableColumn[PointActivityHistoryRepository, String](dataModel.dateColText)
        {
            cellValueFactory = cdf => StringProperty(cdf.value.recCreatedWhen.plusYears(543).toString("dd-MMM-yyyy"))
        }

        val table = new TableView[PointActivityHistoryRepository](historyBuffer)

        Future
        {
            while(table.width.value == 0)
            {
                Thread.sleep(10)
            }
            activityTypeCol.prefWidth = table.width.value * 0.25
            pointCol.prefWidth = table.width.value * 0.5
            dateCol.prefWidth = table.width.value * 0.25
            table.columns ++= List(activityTypeCol, pointCol, dateCol)
        }

        table
    }
}
