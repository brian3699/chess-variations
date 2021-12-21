package ooga.view.game;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ooga.view.components.ButtonFactory;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Objects;

/**
 * Implements the Game Options in the Home Menu.
 *
 * @author Young Jun, Evelyn Cupil-Garcia
 */
public class GameMenuView {

  private Scene gameMenuScene;
  private static final String DEFAULT_RESOURCE_PACKAGE = "ooga.view.resources.CSS.";
  private static final String PAGE_TITLE = "title";
  private static final String DEFAULT_STYLESHEET =
      "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/") + "Default.css";
  private static final String TITLE_ID = "Title2";
  private final ButtonFactory buttonGenerator;
  private final ResourceBundle languageResource;


  /**
   * Constructor for GameOptionsView.
   */
  public GameMenuView(ResourceBundle languageResource) {
    this.languageResource = languageResource;
    buttonGenerator = new ButtonFactory(languageResource);
  }

  /**
   * Generate the menu scene with the buttonMap given from the controller
   *
   * @param buttonMap the Map containing the name of the button and method it should trigger
   * @return MenuScene
   */
  public Scene generateMenuScene(Map<String, EventHandler> buttonMap) {
    VBox root = new VBox();
    HBox title = new HBox(new Text(languageResource.getString(PAGE_TITLE)));
    HBox buttonBox = buttonGenerator.createMultipleButtons(buttonMap);
    title.setId(TITLE_ID);
    root.getChildren().addAll(title, buttonBox);
    gameMenuScene = new Scene(root);
    gameMenuScene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource(DEFAULT_STYLESHEET)).toExternalForm());
    return gameMenuScene;
  }


  /**
   * Getter to return the GameOptionsViewScene.
   *
   * @return gameMenuScene.
   */
  public Scene getScene() {
    return gameMenuScene;
  }

}
