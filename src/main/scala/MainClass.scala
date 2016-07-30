import context.CoreContext
import scene.MainScene

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

/**
  * * # Created by wacharint on 7/23/2016 AD.
  **/
object MainClass extends JFXApp {

    implicit val context = new CoreContext
    implicit val terminator: () => Unit = shutdown

    stage = new PrimaryStage {
        title = "Member Master"
        scene = new MainScene
    }

    def shutdown() = {
        stage.close
    }
}
