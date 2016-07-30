package scene

import javafx.event.{ActionEvent, EventHandler}

import context.CoreContext
import model.{IMemberInfoModel, RegistrationModel}
import org.joda.time.DateTime
import scene.MemberInfoPane.DisplayMode
import scene.MemberInfoPane.DisplayMode.DisplayMode

import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, Label, TextField}
import scalafx.scene.layout.{BorderPane, FlowPane, GridPane}

/**
  * * # Created by wacharint on 7/25/2016 AD.
  **/
class MemberInfoPane(displayMode: DisplayMode)(implicit context: CoreContext) extends BorderPane {

    stylesheets = List(getClass.getResource("/style.css").toExternalForm)

    val dataModel = loadDataModel()
    val gridPane = new GridPane
    val flowPane = new FlowPane

    //<editor-fold desc="Label">

    val headerLabel = new Label(dataModel.headerLabel) {
        id = "header-label"
    }
    top = headerLabel

    val nameLabel = new Label(dataModel.firstNameLabel) {
    }
    gridPane.add(nameLabel, 1, 1)

    val lastNameLabel = new Label(dataModel.lastNameLabel) {
    }

    gridPane.add(lastNameLabel, 1, 2)

    val idLabel = new Label(dataModel.idLabel) {
        id = "id-label"
    }
    gridPane.add(idLabel, 3, 1)

    val telLabel = new Label(dataModel.telLabel) {
        id = "id-label"
    }
    gridPane.add(telLabel, 3, 2)

    val sexLabel = new Label(dataModel.sexLabel) {
        id = "sex-label"
    }
    gridPane.add(sexLabel, 1, 3)

    val birthLabel = new Label(dataModel.birthLabel) {
        id = "birth-label"
    }
    gridPane.add(birthLabel, 3, 3)

    val addressLabel = new Label(dataModel.addressLabel) {
        id = "address-label"
    }
    gridPane.add(addressLabel, 1, 4)

    //</editor-fold>

    //<editor-fold desc="text field">

    val firstNameTextField = new TextField() {
        id = "first-name-text-field"
        text = dataModel.firstName()
        text.onChange {
            dataModel.firstName.update(text.apply())
        }
        onAction = new EventHandler[ActionEvent] {
            override def handle(event: ActionEvent): Unit = {
                lastNameTextField.requestFocus()
            }
        }
    }
    gridPane.add(firstNameTextField, 2, 1)
    var textFieldContents = List(firstNameTextField)

    val lastNameTextField = new TextField() {
        id = "last-name-text-field"
        text = dataModel.lastName()
        text.onChange  {
            dataModel.lastName.update(text.apply())
        }
    }
    gridPane.add(lastNameTextField, 2, 2)
    textFieldContents ++= List(lastNameTextField)

    val idTextField = new TextField() {
        id = "id-text-field"
        text = dataModel.id().toString
        text.onChange {
            text.apply() foreach( c => if(!c.isDigit){
                text = text.apply().replaceAll(c.toString,"")
                alertOnlyNumberAllow
            })
            dataModel.id.update(text.apply().toLong)
        }
    }
    gridPane.add(idTextField, 4, 1)
    textFieldContents ++= List(idTextField)

    val telTextField = new TextField() {
        id = "tel-text-field"
        text = dataModel.tel()
        text.onChange {
            dataModel.tel.update(text.apply())
        }
    }
    gridPane.add(telTextField, 4, 2)
    textFieldContents ++= List(telTextField)

    val sexTextField = new TextField() {
        id = "sex-text-field"

        text = dataModel.sex()
        text.onChange {
            if(text.apply().length > 1) { text = text.apply().charAt(1).toString }
            dataModel.sex.update(text.apply())
        }
    }
    gridPane.add(sexTextField, 2, 3)
    textFieldContents ++= List(sexTextField)

    val birthTextField = new TextField() {
        id = "birth-text-field"

        focused.onChange {

            if(!focused.apply()) {
                var date = DateTime.now()
                try {
                    val parsedDate = DateTime.parse(text.apply())
                    date = parsedDate
                } catch {
                    case e: IllegalArgumentException => {
                        alertIncorrectDateFormat
                        text = ""
                        requestFocus()
                    }
                }
                dataModel.birth.update(date)
            }
        }
    }
    gridPane.add(birthTextField, 4, 3)
    textFieldContents ++= List(birthTextField)

    val addressTextField = new TextField() {
        id = "address-text-field"
        text = dataModel.address()
        text.onChange {
            dataModel.address.update(text.apply())
        }
    }
    gridPane.add(addressTextField, 2, 4)
    textFieldContents ++= List(addressTextField)

    //</editor-fold>

    //<editor-fold desc="button control>

    val saveButton = new Button() {
        id = "save-button"
        text = dataModel.saveButtonText
        onAction = new EventHandler[ActionEvent] {
            override def handle(event: ActionEvent): Unit = {
                saveDataModel()
            }
        }
    }
    var buttonContents = List(saveButton)
    saveButton.visible = dataModel.saveButtonVisible

    val editButton = new Button() {
        id = "edit-button"
        text = dataModel.editButtonText
        onAction = new EventHandler[ActionEvent] {
            override def handle(event: ActionEvent): Unit = ???
        }
    }
    buttonContents ++= List(editButton)
    editButton.visible = dataModel.editButtonVisible

    val clearButton = new Button() {
        id = "clear-button"
        text = dataModel.clearButtonText
        onAction = new EventHandler[ActionEvent] {
            override def handle(event: ActionEvent): Unit = {
                clearData()
            }
        }
    }
    buttonContents ++= List(clearButton)
    clearButton.visible = dataModel.clearButtonVisible

    val inActiveButton = new Button() {
        id = "inactive-button"
        text = dataModel.inActiveButtonText
        onAction = new EventHandler[ActionEvent] {
            override def handle(event: ActionEvent): Unit = ???
        }
    }
    buttonContents ++= List(inActiveButton)
    inActiveButton.visible = dataModel.inActiveButtonVisible

    buttonContents.foreach( b => {
        if(!b.visible.apply()) {
            buttonContents = buttonContents.diff(List(b))
        }
    })

    flowPane.id = "button-control-flow-pane"
    flowPane.children = buttonContents

    //</editor-fold>

    center = gridPane
    bottom = flowPane

    def loadDataModel(): IMemberInfoModel = {
        if(displayMode == DisplayMode.Register){
            new RegistrationModel
        } else {
            null
        }
    }

    def clearData() = {
        textFieldContents.foreach(c => c.asInstanceOf[TextField].text = "")
    }

    def saveDataModel(): Unit = {

        dataModel.save
    }

    def alertOnlyNumberAllow = new Alert(AlertType.Warning,"Only number allowed") {
        title = "Alert"
        headerText = "Validation Error"
    }.showAndWait()

    def alertIncorrectDateFormat = new Alert(AlertType.Warning, """Please enter date in format "yyyy-mm-dd".""") {
        title = "Alert"
        headerText = "Incorrect Date Format"
    }.showAndWait()
}

object MemberInfoPane {
    object DisplayMode extends Enumeration {
        type DisplayMode = Value
        val Register, View, Edit = Value
    }
}
