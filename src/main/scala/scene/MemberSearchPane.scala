package scene

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.input.MouseEvent

import context.CoreContext
import model.MemberSearchModel
import repository.MemberRepository

import scala.concurrent.Future
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, FlowPane}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * * # Created by wacharint on 7/27/2016 AD.
  **/
class MemberSearchPane(openMemberDetailCallback: (MemberRepository) => Unit)(implicit context: CoreContext, terminator: () => Unit) extends BorderPane
{
    stylesheets = List(getClass.getResource("/style.css").toExternalForm)

    var model = new MemberSearchModel(this)
    id = "member-search-tab"

    center = generateMainPane

    def generateMainPane: BorderPane =
    {

        val pane = new BorderPane()
        {
            left = generateMemberSearchPane()
            center = generateSearchResultPane()
        }
        pane
    }

    def generateSearchResultPane(): TableView[MemberRepository] =
    {

        val members = ObservableBuffer(model.members)

        val nameCol = new TableColumn[MemberRepository, String](model.nameColumnText)
        {
            cellValueFactory = cdf => StringProperty(cdf.value.firstName + " " + cdf.value.lastName)
        }
        val idCol = new TableColumn[MemberRepository, Long](model.idColumnText)
        {
            cellValueFactory = cdf => ObjectProperty(cdf.value.id)
        }
        val telCol = new TableColumn[MemberRepository, String](model.telColumnText)
        {
            cellValueFactory = cdf => StringProperty(cdf.value.tel)
        }

        val table = new TableView[MemberRepository](members)

        Future
        {
            while (table.width.value == 0)
            {
                Thread.sleep(5)
            }
            nameCol.prefWidth = table.width.value / 3
            idCol.prefWidth = table.width.value / 3
            telCol.prefWidth = table.width.value / 3
            table.columns ++= List(nameCol, idCol, telCol)
        }


        table.onMouseClicked = new EventHandler[MouseEvent]
        {
            override def handle(event: MouseEvent): Unit =
            {
                if (event.getClickCount == 2)
                {
                    val selectedMember = table.selectionModel.apply().selectedItemProperty().get

                    openMemberDetailCallback(selectedMember)
                }
            }
        }

        table
    }

    def generateMemberSearchPane(): FlowPane =
    {

        val searchLabel = new Label(model.keywordLabel)

        val searchTextField = new TextField
        {
            text = model.searchKeyword()

            onAction = new EventHandler[ActionEvent]
            {
                override def handle(event: ActionEvent): Unit =
                    model.searchKeyword.update(text.apply())
            }
        }

        Future
        {
            while (searchTextField.width.value == 0)
            {
                Thread.sleep(10)
                searchTextField.requestFocus()
            }
        }

        val searchButton = new Button(model.searchButton)
        {
            onAction = new EventHandler[ActionEvent]
            {
                override def handle(event: ActionEvent): Unit =
                {
                    model.searchKeyword.update(searchTextField.text.apply())
                }
            }
        }

        val pane = new FlowPane
        {
            children = List(searchLabel, searchTextField, searchButton)
        }
        pane
    }
}
