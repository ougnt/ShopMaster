package scene

import javafx.event.{ActionEvent, EventHandler}

import context.CoreContext
import model.{IMemberInfoModel, RegistrationModel}
import org.joda.time.DateTime
import scene.MemberInfoPane.DisplayMode
import scene.MemberInfoPane.DisplayMode.DisplayMode

import scala.concurrent.{ExecutionContext, Future}
import scala.math.Ordering.IntOrdering
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, FlowPane, GridPane}

/**
  * * # Created by wacharint on 7/25/2016 AD.
  **/
class MemberInfoPane(displayMode: DisplayMode)(implicit context: CoreContext) extends BorderPane
{

    stylesheets = List(getClass.getResource("/style.css").toExternalForm)
    id = "member-info-pane"

    val dataModel = loadDataModel()
    val gridPane = new GridPane
    val firstNameFlowPane = new FlowPane()
    val lastNameFlowPane = new FlowPane()
    val sexFlowPane = new FlowPane()
    val addressFlowPane = new FlowPane()
    val birthFlowPane = new FlowPane()
    val idFlowPane = new FlowPane()
    val telFlowPane = new FlowPane()
    val buttonFlowPane = new FlowPane

    //<editor-fold desc="Label">

    val nameLabel = new Label(dataModel.firstNameLabel)
    var labelContents = List(nameLabel)

    val lastNameLabel = new Label(dataModel.lastNameLabel)
    labelContents ++= List(lastNameLabel)

    val idLabel = new Label(dataModel.idLabel)
    {
        id = "id-label"
    }
    labelContents ++= List(idLabel)

    val telLabel = new Label(dataModel.telLabel)
    {
        id = "id-label"
    }
    labelContents ++= List(telLabel)

    val sexLabel = new Label(dataModel.sexLabel)
    {
        id = "sex-label"
    }
    labelContents ++= List(sexLabel)

    val birthLabel = new Label(dataModel.birthLabel)
    {
        id = "birth-label"
    }
    labelContents ++= List(birthLabel)

    val addressLabel = new Label(dataModel.addressLabel)
    {
        id = "address-label"
    }
    labelContents ++= List(addressLabel)

    //</editor-fold>

    //<editor-fold desc="text field">

    val firstNameTextField = new TextField()
    {
        id = "first-name-text-field"
        text = dataModel.firstName()
        text.onChange
        {
            dataModel.firstName.update(text.apply())
        }
        onAction = new EventHandler[ActionEvent]
        {
            override def handle(event: ActionEvent): Unit =
            {
                lastNameTextField.requestFocus()
            }
        }
    }
    var textFieldContents = List(firstNameTextField)

    val lastNameTextField = new TextField()
    {
        id = "last-name-text-field"
        text = dataModel.lastName()
        text.onChange
        {
            dataModel.lastName.update(text.apply())
        }
    }
    textFieldContents ++= List(lastNameTextField)

    val idTextField = new TextField()
    {
        id = "id-text-field"
        text = dataModel.id().toString
        text.onChange
        {
            text.apply() foreach (c => if (!c.isDigit)
            {
                text = text.apply().replaceAll(c.toString, "")
                alertOnlyNumberAllow
            })
            dataModel.id.update(text.apply().toLong)
        }
    }
    textFieldContents ++= List(idTextField)

    val telTextField = new TextField()
    {
        id = "tel-text-field"
        text = dataModel.tel()
        text.onChange
        {
            dataModel.tel.update(text.apply())
        }
    }
    textFieldContents ++= List(telTextField)

    val sexTextField = new TextField()
    {
        id = "sex-text-field"

        text = dataModel.sex()
        text.onChange
        {
            if (text.apply().length > 1)
            {
                text = text.apply().charAt(1).toString
            }
            dataModel.sex.update(text.apply())
        }
    }
    textFieldContents ++= List(sexTextField)

    val birthTextField = new TextField()
    {
        id = "birth-text-field"

        focused.onChange
        {

            if (!focused.apply())
            {
                var date = DateTime.now()
                try
                {
                    val parsedDate = DateTime.parse(text.apply())
                    date = parsedDate
                } catch
                {
                    case e: IllegalArgumentException =>
                    {
                        alertIncorrectDateFormat
                        text = ""
                        requestFocus()
                    }
                }
                dataModel.birth.update(date)
            }
        }
    }
    textFieldContents ++= List(birthTextField)

    val addressTextField = new TextField()
    {
        id = "address-text-field"
        text = dataModel.address()
        text.onChange
        {
            dataModel.address.update(text.apply())
        }
    }
    textFieldContents ++= List(addressTextField)

    //</editor-fold>

    //<editor-fold desc="button control>

    val saveButton = new Button()
    {
        id = "save-button"
        text = dataModel.saveButtonText
        onAction = new EventHandler[ActionEvent]
        {
            override def handle(event: ActionEvent): Unit =
            {
                saveDataModel()
            }
        }
    }
    var buttonContents = List(saveButton)
    saveButton.visible = dataModel.saveButtonVisible

    val editButton = new Button()
    {
        id = "edit-button"
        text = dataModel.editButtonText
        onAction = new EventHandler[ActionEvent]
        {
            override def handle(event: ActionEvent): Unit = ???
        }
    }
    buttonContents ++= List(editButton)
    editButton.visible = dataModel.editButtonVisible

    val clearButton = new Button()
    {
        id = "clear-button"
        text = dataModel.clearButtonText
        onAction = new EventHandler[ActionEvent]
        {
            override def handle(event: ActionEvent): Unit =
            {
                clearData()
            }
        }
    }
    buttonContents ++= List(clearButton)
    clearButton.visible = dataModel.clearButtonVisible

    val inActiveButton = new Button()
    {
        id = "inactive-button"
        text = dataModel.inActiveButtonText
        onAction = new EventHandler[ActionEvent]
        {
            override def handle(event: ActionEvent): Unit = ???
        }
    }
    buttonContents ++= List(inActiveButton)
    inActiveButton.visible = dataModel.inActiveButtonVisible

    buttonContents.foreach(b =>
    {
        if (!b.visible.apply())
        {
            buttonContents = buttonContents.diff(List(b))
        }
    })

    buttonFlowPane.id = "button-control-flow-pane"
    buttonFlowPane.children = buttonContents

    //</editor-fold>

    firstNameFlowPane.children = List(nameLabel, firstNameTextField)
    lastNameFlowPane.children = List(lastNameLabel, lastNameTextField)
    sexFlowPane.children = List(sexLabel, sexTextField)
    addressFlowPane.children = List(addressLabel, addressTextField)
    idFlowPane.children = List(idLabel, idTextField)
    telFlowPane.children = List(telLabel, telTextField)
    birthFlowPane.children = List(birthLabel, birthTextField)

    val flowPanes = List(firstNameFlowPane, lastNameFlowPane, sexFlowPane, addressFlowPane, idFlowPane, telFlowPane, birthFlowPane)

    //    val futureTask = Future{
    //        Thread.sleep(1000)
    //    }

    //    futureTask.onComplete  {
    //
    //    }

    gridPane.add(firstNameFlowPane, 1, 1)
    gridPane.add(lastNameFlowPane, 1, 2)
    gridPane.add(sexFlowPane, 1, 3)
    gridPane.add(addressFlowPane, 1, 4)

    gridPane.add(idFlowPane, 2, 1)
    gridPane.add(telFlowPane, 2, 2)
    gridPane.add(birthFlowPane, 2, 3)
    center = gridPane
    bottom = buttonFlowPane

    implicit val executionContext = ExecutionContext.global
    Future
    {
        while (nameLabel.width.apply() == 0d)
        {
            Thread.sleep(500)
        }
        implicit val order = new IntOrdering
        {}
        val longestWidth: Double = labelContents.maxBy(l => l.width.apply()).width.apply()

        flowPanes.foreach(f =>
        {
            f.hgap = longestWidth + 5 - f.children.get(0).asInstanceOf[javafx.scene.control.Label].getWidth
        })

    }

    def loadDataModel(): IMemberInfoModel =
    {
        if (displayMode == DisplayMode.Register)
        {
            new RegistrationModel
        } else
        {
            null
        }
    }

    def clearData() =
    {
        textFieldContents.foreach(c => c.asInstanceOf[TextField].text = "")
    }

    def saveDataModel(): Unit =
    {

        dataModel.save
    }

    def alertOnlyNumberAllow = new Alert(AlertType.Warning, "Only number allowed")
    {
        title = "Alert"
        headerText = "Validation Error"
    }.showAndWait()

    def alertIncorrectDateFormat = new Alert(AlertType.Warning, """Please enter date in format "yyyy-mm-dd".""")
    {
        title = "Alert"
        headerText = "Incorrect Date Format"
    }.showAndWait()
}

object MemberInfoPane
{

    object DisplayMode extends Enumeration
    {
        type DisplayMode = Value
        val Register, View, Edit = Value
    }

}
