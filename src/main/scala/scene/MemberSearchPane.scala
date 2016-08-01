package scene

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.input.MouseEvent

import context.CoreContext
import model.MemberSearchModel
import repository.MemberRepository

import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{Label, TableColumn, TableView, TextField}
import scalafx.scene.layout.{BorderPane, FlowPane}

/**
  * * # Created by wacharint on 7/27/2016 AD.
  **/
class MemberSearchPane(implicit context: CoreContext, terminator: () => Unit) extends BorderPane
{
    //    stylesheets = List(getClass.getResource("/style.css").toExternalForm)

    var model = new MemberSearchModel(this)

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
        {
            columns ++= List(nameCol, idCol, telCol)
        }

        table.onMouseClicked = new EventHandler[MouseEvent]
        {
            override def handle(event: MouseEvent): Unit =
            {
                if (event.getClickCount == 2)
                {
                    val selectedMember = table.selectionModel.apply().selectedItemProperty().get

                    Console.print(selectedMember.firstName + " " + selectedMember.lastName)
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

        val pane = new FlowPane
        {
            children = List(searchLabel, searchTextField)
        }
        pane
    }
}
