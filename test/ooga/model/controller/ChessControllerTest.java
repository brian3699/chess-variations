package ooga.model.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;
import javax.management.ReflectionException;
import ooga.model.board.CheckersBoard;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

class ChessControllerTest extends DukeApplicationTest {
  private Controller c;
  Stage pStage;
  MenuController m;
  File f;

  @Override
  public void start(Stage PrimaryStage) throws ReflectionException {
    pStage =new Stage();
    f = new File("data/boards/Chess_board.csv");
    m= new MenuController(pStage);
    m.startOptionsView();
    m.startGame("Chess", f);
    c= m.getController();
    pStage.hide();
  }

  @Test
  void initializeGameController()
      throws ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertNotNull(c);
  }

  @Test
  void getGameClass() {
    assertEquals("Chess",c.getGameClass());
  }

  @Test
  void onCellClickAndMakeMove() {
    runAsJFXAction(()->c.onCellClick(new Point(2,0)));
    assertFalse(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(1,0)));
    assertFalse(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(6,0)));
    assertTrue(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(4,0)));
    assertNull(c.getMyBoard().getPieceAt(6,0));
    assertNotNull(c.getMyBoard().getPieceAt(4,0));
    assertFalse(c.getPieceChosen());
//    runAsJFXAction(()->c.onCellClick(new Point(3,1)));
//    assertFalse(c.getPieceChosen());
  }


}