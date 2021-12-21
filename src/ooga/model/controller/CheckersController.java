package ooga.model.controller;

import com.opencsv.exceptions.CsvValidationException;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javax.management.ReflectionException;
import ooga.model.board.BoardInterface;
import ooga.model.board.CheckersBoard;
import ooga.view.components.MessageBuilder;
import ooga.view.game.GameView;
import ooga.view.game.GameViewInterface;

public class CheckersController extends Controller {

  private static final String CHECKERS = "Checkers";
  private static final String END_TURN = "EndTurn";
  public static final char CHECKERS_KING = 'X';
  private ResourceBundle languageResource;
  BoardInterface myBoard;
  GameViewInterface myView;
  private MessageBuilder messageBuilder;


  public CheckersController(EventHandler event, String gameType, ResourceBundle languageResource,
      File file)
      throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    super(event, gameType, languageResource, file);
    messageBuilder = new MessageBuilder(languageResource);
    myBoard = getMyBoard();
    myView = getView();
  }

  /**
   * get buttons unique to checkers
   */
  @Override
  protected void getGameButtons() {
    addButton(END_TURN, e -> setTurnOver(true));
  }

  /**
   * gets the game type name
   *
   * @return
   */
  @Override
  protected String getGameClass() {
    return CHECKERS;
  }

  /**
   * abstract method that chooses the piece chosen, finds valid moves and moves/jumps pieces when
   * the user clicks
   *
   * @param coordinate is the x,y position of where the user hs clicked
   */
  @Override
  protected void onCellClick(Point coordinate) {
    int x = (int) coordinate.getX();
    int y = (int) coordinate.getY();
    if (hasPieceAt(x, y) && isCorrectTeam(x, y) && !getPieceChosen()) {
      updatePieceChosen(x, y);
    } else if (getPieceChosen() && isInValidMoves(x, y)) {
      makeMove(x, y);
    } else if (hasPieceAt(x, y) && !isCorrectTeam(x, y)) {
      messageBuilder.showMessage(INCORRECT_TEAM);
    } else if (!getPieceChosen()) {
      messageBuilder.showMessage(SELECT_PIECE);
    } else if (getPieceChosen() && !((CheckersBoard) myBoard).getTurnOver()) {
      sendValidMoves();
      setPieceChosen(true);
    } else {
      messageBuilder.showMessage(INVALID_MOVE); // TODO move to language resources
    }
  }

  /**
   * updates the piece by moving it on the board and switching teams if applicable
   *
   * @param r row of the move
   * @param c column of the move
   */
  @Override
  protected void makeMove(int r, int c) {
    myView.removePossibleMoves(myBoard.getValidMoves());
    myBoard.movePiece(r, c);
    myView.updateCell(r, c, myBoard.getPieceAt(r, c));
    myView.updateCell(
        myBoard.getCurrentRow(),
        myBoard.getCurrentCol(),
        myBoard.getPieceAt(myBoard.getCurrentRow(), myBoard.getCurrentCol()));
    if (((CheckersBoard) myBoard).getJumped()) {
      myView.updateCell(
          r + ((myBoard.getCurrentRow() - r) / 2),
          c + ((myBoard.getCurrentCol() - c) / 2),
          null);
    }
    myView.updateScore(myBoard.getActiveTeam().getTeamNum(), myBoard.getActiveTeam().getScore());
    checkIfTeamWon();
    checkIfTurnIsOver(r, c);
  }

  private void checkIfTurnIsOver(int r, int c) {
    ((CheckersBoard) (myBoard)).setIfTurnOver(r, c);
    if (((CheckersBoard) myBoard).getTurnOver()) {
      myBoard.setActiveTeam();
      myView.setActiveTeam(myBoard.getActiveTeam().getTeamNum());
      setPieceChosen(false);
    } else {
      setPieceChosen(true);
      onCellClick(new Point(r, c));
    }
  }

  private void setTurnOver(boolean t) {
    ((CheckersBoard) getMyBoard()).setTurnOver(t);
    myView.removePossibleMoves(myBoard.getValidMoves());
    myBoard.setActiveTeam();
    myView.setActiveTeam(myBoard.getActiveTeam().getTeamNum());
    setPieceChosen(false);
  }

  @Override
  protected void setPawnPromotion(){
    if (!myBoard.checkPawnPromotion()) {
      messageBuilder.showMessage(INVALID_MOVE);
    }else{
      myBoard.promotePawn(CHECKERS_KING);
      myView.initializePiece(myBoard.getPawnPromotionPiece(), myBoard.getPieceRow(myBoard.getPawnPromotionPiece()), myBoard.getPieceCol(myBoard.getPawnPromotionPiece()));
      myBoard.setActiveTeam();
      myView.setActiveTeam(myBoard.getActiveTeam().getTeamNum());
      setPieceChosen(false);
    }

  }

}
