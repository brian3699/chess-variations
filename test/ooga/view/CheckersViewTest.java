package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.management.ReflectionException;
import ooga.model.controller.MenuController;
import org.junit.jupiter.api.Test;
import org.testfx.matcher.base.NodeMatchers;
import util.DukeApplicationTest;

public class CheckersViewTest extends DukeApplicationTest {
  /**
   * Start method to run tests.
   * @param stage stage to display.
   */
  @Override
  public void start(Stage stage)
      throws ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    File checkers = new File("data/boards/Checkers_board_Double_Turn.csv");
    MenuController mainController = new MenuController(stage);
    mainController.startGame("Checkers", checkers);
  }

  /**
   * Test that checks that we can do a double turn.
   */
  @Test
  public void doubleTurnPossible() {
    GridPane grid = lookup("#Board").query();
    clickOn(grid.getChildren().get(40));
    clickOn(grid.getChildren().get(26));
    assertEquals(grid.getChildren().get(12).getStyle(), "-fx-background-color: #00FF00");
  }

  /**
   * Test that ends a double turn and we click on red again, but we get an error because it's black's turn.
   */
  @Test
  public void endDoubleTurn() {
    GridPane grid = lookup("#Board").query();
    clickOn(grid.getChildren().get(40));
    clickOn(grid.getChildren().get(26));
    Button endTurn = lookup("#EndTurn").query();
    clickOn(endTurn);
    clickOn(grid.getChildren().get(39));
    verifyThat("OK", NodeMatchers.isVisible());
  }
}
