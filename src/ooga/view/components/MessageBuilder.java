package ooga.view.components;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * Creates a popup with a message and displays it to the user
 *
 * @author Young Jun
 */
public class MessageBuilder {

  private ButtonFactory buttonFactory;
  private ResourceBundle languageResource;


  public MessageBuilder(ResourceBundle languageResource) {
    this.languageResource = languageResource;
    buttonFactory = new ButtonFactory(languageResource);
  }

  /**
   * Shows an error message with the message
   *
   * @param message message to be displayed to the user
   */
  public void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(languageResource.getString(message));
    alert.setContentText(languageResource.getString(message));
    alert.showAndWait();
  }

  /**
   * Creates a popup scene with buttons
   *
   * @param title     title of the scene
   * @param message   message displayed
   * @param buttonMap Map of buttons to be displayed
   */
  public void showPopupScene(String title, String message, Map<String, EventHandler> buttonMap) {
    Stage popup = new Stage();
    popup.setTitle(languageResource.getString(title));
    VBox content = new VBox();
    HBox buttons = buttonFactory.createMultipleButtons(buttonMap);
    buttons.getChildren().get(0).setOnMouseClicked(event -> popup.close());
    content.getChildren().addAll(new Text(languageResource.getString(message)), buttons);
    Scene root = new Scene(content);
    popup.setScene(root);
    popup.show();
  }

}
