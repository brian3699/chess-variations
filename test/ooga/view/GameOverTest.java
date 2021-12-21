package ooga.view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.management.ReflectionException;
import ooga.model.controller.MenuController;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class GameOverTest extends DukeApplicationTest {
  @Override
  public void start(Stage stage)
      throws IOException, ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    File chess = new File("data/boards/Chess_board_Game_Over.csv");
    MenuController mainController = new MenuController(stage);
    mainController.startGame("Chess", chess);
  }

  /**
   * Test that checks that end game has been reached when the king has been captured.
   */
  @Test
  public void endGameScreen() {
    GridPane grid = lookup("#Board").query();
    clickOn(grid.getChildren().get(11));
    clickOn(grid.getChildren().get(4));
    Button home = lookup("#Home").query();
    assertTrue(home.isVisible());
  }

  /**
   * Test that a piece is captured.
   */
  @Test
  public void pieceCaptured() {
    GridPane grid = lookup("#Board").query();
    clickOn(grid.getChildren().get(11));
    clickOn(grid.getChildren().get(2));
    assertTrue(!grid.getChildren().get(11).equals(grid.getChildren().get(2)));
  }


}
