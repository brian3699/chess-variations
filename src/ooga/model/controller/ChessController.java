package ooga.model.controller;

import com.opencsv.exceptions.CsvValidationException;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javax.management.ReflectionException;
import ooga.model.board.BoardInterface;
import ooga.model.board.ChessBoard;
import ooga.view.components.MessageBuilder;
import ooga.view.components.UserInputBuilder;
import ooga.view.game.GameView;
import ooga.view.game.GameViewInterface;


public class ChessController extends Controller {
  private static final String FOUR_PLAYER_CHESS = "FourPlayerChess";
  private static final String PAWN_PROMOTION = "PawnPromotion";
  private static final String PAWN_PROMOTION_2 = "PawnPromotion2";
  private static final String CASTLING = "Castling";
  private static final String CHECK = "Check";
  public static final String CHESS = "Chess";
  private BoardInterface myBoard;
  private GameViewInterface myView;
  private MessageBuilder messageBuilder;
  private UserInputBuilder userInputBuilder;
  private ResourceBundle languageResource;



  public ChessController(EventHandler event, String gameType, ResourceBundle languageResource,
      File file)
      throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    super(event, gameType, languageResource, file);
    messageBuilder = new MessageBuilder(languageResource);
    myBoard = getMyBoard();
    myView = getView();
    userInputBuilder = new UserInputBuilder(languageResource);
    this.languageResource = languageResource;
  }

  /**
   * get buttons unique to chess games
   */
  @Override
  protected void getGameButtons() {
    addButton(CASTLING, e -> setCastling());
  }

  /**
   * get the game class name
   *
   * @return
   */
  @Override
  protected String getGameClass() {
    return CHESS;
  }

  /**
   * updates the piece by moving it on the board and switching teams if applicable
   *
   * @param x row of the move
   * @param y column of the move
   */
  @Override
  protected void makeMove(int x, int y) {
    myView.removePossibleMoves(myBoard.getValidMoves());
    myBoard.movePiece(x, y); // x,y are a highlighted cell of new piece position
    System.out.println(myBoard.getCurrentRow() + " " + myBoard.getCurrentCol());
    myView.updateCell(x, y, myBoard.getPieceAt(x, y));
    myView.updateCell(
        myBoard.getCurrentRow(),
        myBoard.getCurrentCol(),
        myBoard.getPieceAt(myBoard.getCurrentRow(), myBoard.getCurrentCol()));
    myView.updateScore(myBoard.getActiveTeam().getTeamNum(), myBoard.getActiveTeam().getScore());
    nextMove();
  }

  private void checkIfCurrentlyInCheck() {
    if(((ChessBoard)myBoard).checkForCheck()){
      messageBuilder.showMessage(CHECK);
    }
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
    if (hasPieceAt(x, y) && isCorrectTeam(x, y)) {
      updatePieceChosen(x, y);
    } else if (getPieceChosen() && isInValidMoves(x, y)) {
      makeMove(x, y);
    } else if (hasPieceAt(x, y) && !isCorrectTeam(x, y)) {
      messageBuilder.showMessage(INCORRECT_TEAM);
    } else if (!getPieceChosen()) {
      messageBuilder.showMessage(SELECT_PIECE);
    } else {
      messageBuilder.showMessage(INVALID_MOVE); // TODO move to language resources
    }
    //TODO callable pair?

  }

  private void setCastling() {
    if (((ChessBoard)myBoard).canCastleLeftRook()) {
      ((ChessBoard)myBoard).castleLeftRook();
      myBoard.setActiveTeam();
      myView.setActiveTeam(myBoard.getActiveTeam().getTeamNum());
      setPieceChosen(false);
    }else if(((ChessBoard)myBoard).canCastleRightRook()){
      ((ChessBoard)myBoard).castleRightRook();
      myBoard.setActiveTeam();
      myView.setActiveTeam(myBoard.getActiveTeam().getTeamNum());
      setPieceChosen(false);
    }else{
      messageBuilder.showMessage(INVALID_MOVE);
    }
  }

  private void nextMove(){
    checkIfTeamWon();
    checkIfCurrentlyInCheck();
    myBoard.setActiveTeam();
    myView.setActiveTeam(myBoard.getActiveTeam().getTeamNum());
    setPieceChosen(false);
  }

}
