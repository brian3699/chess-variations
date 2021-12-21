package ooga.view;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.management.ReflectionException;
import ooga.model.controller.MenuController;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.io.IOException;

class SavedGameTest extends DukeApplicationTest {
    private static final String DEFAULT_LANGUAGE_FILE = "ooga.view.resources.language.English";

    @Override
    public void start(Stage stage)
        throws IOException, ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
      File chess = new File("data/savedFile/18\uF03A27\uF03A06.csv");
      MenuController mainController = new MenuController(stage);
      mainController.startGame("Chess", chess);
    }

  /**
   * Test that checks that we started from game uploaded given a file that was saved.
   */
  @Test
    public void startFromGameUpload() {
      Button saveGame = lookup("#SaveGame").query();
      assertTrue(saveGame.isVisible());
    }


}