package CharitySystem

import javafx.application.Application
import javafx.stage.Stage
import CharitySystem.view.SceneNavigator

/** Pure JavaFX bootstrap (no ScalaFX). */
class MainApp extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Food Donation Tracker")
    primaryStage.setResizable(true)

    // Give the ONE primary stage to the navigator, then show Login
    SceneNavigator.init(primaryStage)
    SceneNavigator.show("Login.fxml")
  }
}
