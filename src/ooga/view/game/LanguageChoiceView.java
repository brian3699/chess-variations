package ooga.view.game;

import javafx.scene.control.ChoiceDialog;
import ooga.view.components.UserInputBuilder;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Implements the Language Options in the Home Menu.
 *
 * @author Young Jun, Evelyn Cupil-Garcia
 */
public class LanguageChoiceView {

  private static final String DEFAULT_LANGUAGE_DIRECTORY = "ooga.view.resources.language.";
  private static final String LANGUAGE_LIST_DIRECTORY = "ooga.view.resources.language.LanguageList";
  private static final String LANGUAGE_OPTION_TITLE = "Please Select a Language ";
  private static final String DEFAULT_LANGUAGE = "English";
  private List<String> languageOptions;
  private ResourceBundle languageResource;
  private UserInputBuilder userInputBuilder;


  /**
   * Constructor for LanguageChoiceView.
   */
  public LanguageChoiceView() {
    languageOptions = new ArrayList<>();
    populateLanguageOptions();
    userInputBuilder = new UserInputBuilder();
  }

  // read from LANGUAGE_LIST_DIRECTORY and add elements to languageOptions arraylist
  private void populateLanguageOptions() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(LANGUAGE_LIST_DIRECTORY);
    Enumeration<String> enumeration = resourceBundle.getKeys();
    while (enumeration.hasMoreElements()) {
      languageOptions.add(enumeration.nextElement());
    }
  }

  /**
   * Create choice dialog box to choose the language of the game and return ResourceBundle of the
   * selected language
   *
   * @return ResourceBundle of the selected language
   */
  public ResourceBundle generateLanguageOptions() {
    ChoiceDialog<String> langDialog = userInputBuilder.makeChoiceDialog(DEFAULT_LANGUAGE,
        languageOptions, LANGUAGE_OPTION_TITLE);
    langDialog.showAndWait();
    languageResource = makeResourceBundle(langDialog.getSelectedItem());
    return languageResource;
  }

  // make ResourceBundle and return
  private ResourceBundle makeResourceBundle(String lang) {
    try {
      return ResourceBundle.getBundle(DEFAULT_LANGUAGE_DIRECTORY + lang);
    } catch (Exception e) {
      return ResourceBundle.getBundle(DEFAULT_LANGUAGE_DIRECTORY + DEFAULT_LANGUAGE);
    }
  }
}
