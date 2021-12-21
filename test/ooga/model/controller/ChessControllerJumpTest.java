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

class ChessControllerJumpTest extends DukeApplicationTest {
  private Controller c;
  Stage pStage;
  MenuController m;
  File f;

  @Override
  public void start(Stage PrimaryStage) throws ReflectionException {
    pStage =PrimaryStage;
    f = new File("data/boards/Chess_board_oneMove.csv");
    m= new MenuController(pStage);
    m.startOptionsView();
    m.startGame("Chess", f);
    c= m.getController();
    pStage.hide();
  }


  @Test
  void onCellClickAndMakeMove() {
    runAsJFXAction(() -> c.onCellClick(new Point(6, 0)));
    runAsJFXAction(() -> c.onCellClick(new Point(5, 0)));
    runAsJFXAction(() -> c.onCellClick(new Point(1, 2)));
    assertTrue(c.getPieceChosen());
    runAsJFXAction(() -> c.onCellClick(new Point(2, 1)));
    assertNull(c.getMyBoard().getPieceAt(1, 2));
    assertNotNull(c.getMyBoard().getPieceAt(2, 1));
    assertFalse(c.getPieceChosen());
    }



}