package CharitySystem.view

import javafx.fxml.FXMLLoader
import javafx.scene.{Parent => JParent, Scene}
import javafx.stage.Stage

object SceneNavigator {
  private var primary: Stage = _

  def init(stage: Stage): Unit = { primary = stage }

  def show(fxmlName: String): Unit = {
    require(primary != null, "SceneNavigator.init(...) was not called")
    val url = getClass.getResource(s"/CharitySystem/view/$fxmlName")
    val root: JParent = FXMLLoader.load(url)
    primary.setScene(new Scene(root))
    primary.show()
  }

  def goTo(fxmlName: String): Unit = show(fxmlName) // alias
}
