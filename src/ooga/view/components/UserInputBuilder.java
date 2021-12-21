package ooga.view.components;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Builds scene to receive user input
 *
 * @author Young Jun, Evelyn Cupil-Garcia
 */
public class UserInputBuilder {

  private static final String DEFAULT_LANGUAGE_FILE = "ooga.view.resources.language.English";
  private static final String COLOR_PICKER_ID1 = "BoardColorOne";
  private static final String COLOR_PICKER_ID2 = "BoardColorTwo";
  private static final String COLOR_PICKER_TITLE = "CustomBoardColor";

  private ResourceBundle languageBundle;

  public UserInputBuilder(ResourceBundle languageBundle) {
    this.languageBundle = languageBundle;
  }

  public UserInputBuilder() {
    this.languageBundle = ResourceBundle.getBundle(DEFAULT_LANGUAGE_FILE);
  }

  /**
   * creates scene for text input and returns the user's input
   *
   * @param prompt prompt to be shown to the user
   * @return user input
   */
  public String getTextInput(String prompt) {
    try {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setContentText(prompt);
      Optional<String> answer = dialog.showAndWait();
      return answer.get();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Creates a choiceDialog and returns
   *
   * @param defaultOption default option
   * @param optionList    List containing all options
   * @param title         title of the choice dialog
   * @return choice dialog created with parameters
   */
  public ChoiceDialog<String> makeChoiceDialog(String defaultOption, List<String> optionList,
      String title) {
    ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(defaultOption);
    choiceDialog.setContentText(title);
    for (String option : optionList) {
      choiceDialog.getItems().add(option);
    }
    return choiceDialog;
  }

  /**
   * Create choice dialog and return the first character of the user's choice
   *
   * @param defaultOption default option
   * @param optionList    list of options
   * @param title         title of the choice dialog
   * @return user's choice from choice dialog
   */
  public String getDialogInput(String defaultOption, List<String> optionList, String title) {
    ChoiceDialog<String> dialog = makeChoiceDialog(defaultOption, optionList, title);
    dialog.showAndWait();
    return languageBundle.getString(dialog.getSelectedItem());
  }

  /**
   * Create a scene for user to choose two colors and return's user's choice
   *
   * @return list of colors the user chose
   */
  public List<Color> getUserColorChoice() {
    try {
      Stage colorStage = new Stage();
      VBox root = new VBox();
      ColorPicker colorPicker1 = new ColorPicker();
      colorPicker1.setId(COLOR_PICKER_ID1);
      ColorPicker colorPicker2 = new ColorPicker();
      colorPicker2.setId(COLOR_PICKER_ID2);
      root.getChildren().addAll(new Text(languageBundle.getString(COLOR_PICKER_TITLE)),
          colorPicker1, colorPicker2);
      colorStage.setScene(new Scene(root));
      colorStage.showAndWait();
      List<Color> ret = new ArrayList<>();
      ret.add(colorPicker1.getValue());
      ret.add(colorPicker2.getValue());
      return ret;
    } catch (Exception e) {
      return null;
    }
  }

  private Node setId(String id, Node node) {
    node.setId(id);
    return node;
  }
}
