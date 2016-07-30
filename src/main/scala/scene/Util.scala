package scene

import javafx.event.{ActionEvent, EventHandler}

import scalafx.scene.control.{Menu, MenuItem, MenuBar}

/**
  * * # Created by wacharint on 7/27/2016 AD.
  **/
object Util {

    def generateMenuBar(implicit terminator: () => Unit): MenuBar = {
        val exitItem = new MenuItem() {
            text = "Exit"
            onAction = new EventHandler[ActionEvent] {
                override def handle(event: ActionEvent): Unit = {
                    terminator()
                }
            }
        }
        val menu = new Menu() {
            text = "File"
            items = List(exitItem)
        }
        val menuBar = new MenuBar(){
            prefWidth = 800
            menus = List(menu)
        }
        menuBar
    }
}
