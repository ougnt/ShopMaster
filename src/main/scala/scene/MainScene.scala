package scene

import context.CoreContext
import model.MainModel
import repository.MemberRepository
import scene.MemberInfoPane.DisplayMode

import scalafx.scene.Scene
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.layout.BorderPane

/**
  * * # Created by wacharint on 7/25/2016 AD.
  **/
class MainScene(implicit context: CoreContext, terminator: () => Unit) extends Scene(800, 600)
{
    implicit def menuItemHandler(itemName: String) =
    {
        itemName match
        {
            case MenuUtil.exitMenuItemText => terminator()
            case MenuUtil.memberSearchMenuItemText =>
            {
                tabPane.selectionModel.value.select(memberSearchTab)
            }
            case MenuUtil.registerNewMemberMenuItemText =>
            {
                tabPane.selectionModel.value.select(memberRegistrationTab)
            }
        }
    }

    val model = new MainModel
    stylesheets = List(getClass.getResource("/style.css").toExternalForm)

    val memberSearchScene = new MemberSearchPane(openMemberDetailTabCallback)
    val memberSearchTab = new Tab
    {
        text = model.memberSearchTabText
        content = memberSearchScene
    }
    memberSearchTab.closable = false

    val memberRegistrationTab = new Tab
    {
        text = model.memberRegistrationTabText
        content = new MemberInfoPane(DisplayMode.Register, openMemberDetailTabCallback)
    }
    memberRegistrationTab.closable = false

    val settingTab = new Tab
    {
        text = model.settingTabText
        content = new PointRedeemOptionSettingPane
    }

    val tabPane = new TabPane
    {
        tabs = List(memberSearchTab, memberRegistrationTab) ++ List(settingTab)
    }

    //    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.Unavailable)

    val mainPane = new BorderPane
    {
        top = MenuUtil.generateMenuBar
        center = tabPane
    }

    root = mainPane

    def openMemberDetailTabCallback(member: MemberRepository): Unit =
    {
        val newMemberDetailTab = new Tab
        {
            text = member.firstName + " " + member.lastName

            closable = true
            content = new MemberInfoPane(DisplayMode.Edit, openMemberPointHistoryTabCallback, member.memberId)
        }
        tabPane.tabs.add(newMemberDetailTab)
        tabPane.selectionModel.value.select(newMemberDetailTab)
    }

    def openMemberPointHistoryTabCallback(member: MemberRepository): Unit =
    {
        val newHistoryTab = new Tab
        {
            text = "Point activity transaction of " + member.firstName + " " + member.lastName
            closable = true
            content = new PointActivityHistoryPane(member)
        }
        tabPane.tabs.add(newHistoryTab)
        tabPane.selectionModel.value.select(newHistoryTab)
    }
}
