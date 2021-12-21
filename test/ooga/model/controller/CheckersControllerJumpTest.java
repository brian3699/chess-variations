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

public class CheckersControllerJumpTest extends DukeApplicationTest {
  private Controller c;
  Stage pStage;
  MenuController m;
  File f;

  @Override
  public void start(Stage PrimaryStage) throws ReflectionException {
    pStage =new Stage();
    f = new File("data/boards/Checkers_oneMove_board.csv");
    m= new MenuController(pStage);
//    runAsJFXAction(()->m.startOptionsView());
    m.startOptionsView();
    m.startGame("Checkers", f);
    c= m.getController();
    pStage.hide();
  }

  @Test
  void onCellClick()
      throws ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    runAsJFXAction(()->c.onCellClick(new Point(3,0)));
    assertTrue(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(1,2)));
    //assertTrue(c.getPieceChosen());
    assertTrue(((CheckersBoard)c.getMyBoard()).getTurnOver());
    assertFalse(c.getPieceChosen());
  }

  //3,0 --> 1,2

}
