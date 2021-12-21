package ooga.model.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.management.ReflectionException;
import ooga.model.board.CheckersBoard;
import ooga.view.game.GameMenuView;
import ooga.view.game.LanguageChoiceView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import util.DukeApplicationTest;

class CheckersControllerTest extends DukeApplicationTest {
  private static final String MODEL_PATH = "ooga.model.controller.";
  private static final String CONTROLLER = "Controller";
  private static final CharSequence CHESS = "Chess";
  private static final String CSV_PATH = "data/boards/";
  private static final String START_GAME = "StartNewGame";
  private static final String CHECKERS = "Checkers";
  private static final String CHRISTMAS = "ChristmasChess";
  private static final String BIG_BOARD = "BigBoardChess";
  private static final String FOUR_PLAYER = "FourPlayerChess";
  private static final String PLAY_CHESS ="PlayChess";
  private static final String PLAY_CHECKERS = "PlayCheckers";
  private static final String PLAY_CHRISTMAS = "PlayChristmasChess";
  private static final String PLAY_BIG_BOARD = "PlayBigBoardChess";
  private static final String PLAY_FOUR = "PlayFourPlayerChess";
  private static final String PLAY_CUSTOM = "PlayCustomChess";
  private Controller c;
  Stage pStage;
  MenuController m;
  File f;

  @Override
  public void start(Stage PrimaryStage) throws ReflectionException {
    pStage =new Stage();
    f = new File("data/boards/Checkers_board.csv");
    m= new MenuController(pStage);
    runAsJFXAction(()->m.startOptionsView());
    m.startOptionsView();
    m.startGame("Checkers", f);
    c= m.getController();
    pStage.hide();
  }


  @Test
  void initializeGameController()
      throws ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertNotNull(c);
  }


  @Test
  void getGameClass()
      throws ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    assertEquals("Checkers",((CheckersController)c).getGameClass());
  }

  @Test
  void onCellClickAndMakeMove()
      throws ReflectionException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    runAsJFXAction(()->c.onCellClick(new Point(0,0)));
    assertFalse(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(2,1)));
    assertFalse(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(5,0)));
    assertTrue(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(4,1)));
    assertTrue(((CheckersBoard)c.getMyBoard()).getTurnOver());
    assertNull(c.getMyBoard().getPieceAt(5,0));
    assertNotNull(c.getMyBoard().getPieceAt(6,1));
    assertFalse(c.getPieceChosen());
    runAsJFXAction(()->c.onCellClick(new Point(3,1)));
    assertFalse(c.getPieceChosen());
  }

  @Test
  void endTurn(){
    Button checkers = lookup("#PlayCheckers").query();
    clickOn(checkers);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    Button endTurn = lookup("#EndTurn").query();
    clickOn(endTurn);
    assertTrue(((CheckersBoard)c.getMyBoard()).getTurnOver());
  }

}