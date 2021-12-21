package ooga;


import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Application;
import javafx.stage.Stage;
import ooga.model.controller.MenuController;

import java.io.IOException;

/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Main extends Application {

  /**
   * A method to test (and a joke :).
   */
  private final String TITLE = "OOGA";

  public double getVersion() {
    return 0.001;
  }


  @Override
  public void start(Stage stage) throws NoSuchMethodException, CsvValidationException, IOException {
    MenuController mainController = new MenuController(stage);
    mainController.startLanguageChoiceView();
    mainController.startOptionsView();
  }
}
