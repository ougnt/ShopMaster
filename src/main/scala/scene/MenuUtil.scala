package scene

import javafx.event.{ActionEvent, EventHandler}

import scalafx.scene.control.{Menu, MenuItem, MenuBar}

/**
  * * # Created by wacharint on 7/27/2016 AD.
  **/
object MenuUtil
{
    val fileMenuText = "File"
    val exitMenuItemText = "Exit"
    val navigateMenuText = "Navigate"
    val memberSearchMenuItemText = "Member Search"
    val registerNewMemberMenuItemText = "Register a new member"

    def generateMenuBar(implicit menuItemClickHandler: (String) => Unit): MenuBar =
    {
        val exitItem = new MenuItem()
        {
            text = exitMenuItemText
            onAction = new EventHandler[ActionEvent]
            {
                override def handle(event: ActionEvent): Unit =
                {
                    menuItemClickHandler(exitMenuItemText)
                }
            }
        }
        val fileMenu = new Menu()
        {
            text = fileMenuText
            items = List(exitItem)
        }

        val memberSearchMenuItem = new MenuItem()
        {
            text = memberSearchMenuItemText
            onAction = new EventHandler[ActionEvent]
            {
                override def handle(event: ActionEvent): Unit =
                {
                    menuItemClickHandler(memberSearchMenuItemText)
                }
            }
        }

        val registerNewMemberMenuItem = new MenuItem()
        {
            text = registerNewMemberMenuItemText
            onAction = new EventHandler[ActionEvent] {
                override def handle(event: ActionEvent): Unit =
                {
                    menuItemClickHandler(registerNewMemberMenuItemText)
                }
            }
        }

        val navigationMenu = new Menu()
        {
            text = navigateMenuText
            items = List(memberSearchMenuItem, registerNewMemberMenuItem)
        }

        val menuBar = new MenuBar()
        {
            prefWidth = 800
            menus = List(fileMenu, navigationMenu)
        }
        menuBar
    }
}
