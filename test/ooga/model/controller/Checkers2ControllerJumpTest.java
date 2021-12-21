package ooga.model.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;
import javax.management.ReflectionException;
import ooga.model.board.CheckersBoard;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class Checkers2ControllerJumpTest extends DukeApplicationTest {
  private Controller c;
  Stage pStage;
  MenuController m;
  File f;

  @Override
  public void start(Stage PrimaryStage) throws ReflectionException {
    pStage =PrimaryStage;
    f = new File("data/boards/Checkers_twoMove_board.csv");
    m= new MenuController(pStage);
    m.startOptionsView();
    m.startGame("Checkers", f);
    c= m.getController();
    pStage.hide();
  }

  @Test
  void onCellClick()
      throws ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    runAsJFXAction(()->c.onCellClick(new Point(3,1)));
    assertTrue(c.getPieceChosen());
    assertFalse(((CheckersBoard)c.getMyBoard()).getTurnOver());
    runAsJFXAction(()->c.onCellClick(new Point(1,3)));
    assertTrue(((CheckersBoard)c.getMyBoard()).getTurnOver());
    assertFalse(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(2,4)));
    assertTrue(c.getPieceChosen());
    assertFalse(((CheckersBoard)c.getMyBoard()).getTurnOver());
    runAsJFXAction(()->c.onCellClick(new Point(2,5)));
    assertFalse(((CheckersBoard)c.getMyBoard()).getTurnOver());
    runAsJFXAction(()->c.onCellClick(new Point(4,2)));
    runAsJFXAction(()->c.onCellClick(new Point(7,2)));
    assertFalse(((CheckersBoard)c.getMyBoard()).getTurnOver());

  }

}
