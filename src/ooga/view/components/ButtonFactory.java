package ooga.view.components;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * Create button and return
 *
 * @author Young Jun
 */
public class ButtonFactory {

  private ResourceBundle languageBundle;
  private static final String BUTTON_ID = "Buttonbox";

  public ButtonFactory(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
  }

  /**
   * Create multiple buttons from a map and return a Hbox containing all buttons
   *
   * @param buttonMap Map of button name and action method
   * @return HBox containing all generated buttons
   */
  public HBox createMultipleButtons(Map<String, EventHandler> buttonMap) {
    HBox retBox = new HBox();
    for (String s : buttonMap.keySet()) {
      retBox.getChildren().add(createButton(s, buttonMap.get(s)));
    }
    retBox.setId(BUTTON_ID);
    return retBox;
  }

  /**
   * Create a button and returns it
   *
   * @param name  name of the button
   * @param event method to be triggered when the button is clicked
   * @return button created from this method
   */
  public Button createButton(String name, EventHandler event) {
    String buttonName = languageBundle.getString(name);
    Button button = new Button(buttonName);
    button.setOnAction(event);
    return (Button) setId(name, button);
  }

  private Node setId(String id, Node node) {
    node.setId(id);
    return node;
  }
}
