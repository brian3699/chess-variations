package ooga.model.controller;

import com.opencsv.exceptions.CsvValidationException;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import ooga.model.board.Board;
import ooga.model.board.BoardInterface;
import ooga.model.board.GameAreaEditorBoard;
import ooga.model.util.Grid;
import ooga.model.util.Move;
import ooga.util.CSVParser;
import ooga.view.components.MessageBuilder;
import ooga.view.game.GameView;
import ooga.view.game.GameViewInterface;

import javax.management.ReflectionException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class GameAreaEditorController extends Controller {

  private static final String PROPERTY_PATH = "ooga/model/resources/";
  private static final String PROPERTIES = ".properties";
  private static final String CSV_PATH = "data/boards/";
  private static final int MAX_ALLOWED_TEAMS = 4;
  private GameAreaEditorBoard myBoard;
  private GameViewInterface myView;
  private EventHandler home;
  private boolean deleteActive;
  private MessageBuilder myMessageBuilder;

  private int teamNum;


  public GameAreaEditorController(EventHandler event, String gameType,
      ResourceBundle languageResource, File file)
      throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    super(event, gameType, languageResource, file);

    myMessageBuilder = new MessageBuilder(languageResource);
    myView = getView();
    //TODO: Modify gameType based on user input on size of the board
//        String boardIdentifier = gameType + "/" + "10x10";
  }

  /**
   * @return a String with the specified name of the specific Board class
   */
  @Override
  protected String getGameClass() {
    return "GameAreaEditor";
  }


  /**
   * TODO: Execute the move
   * Chosen Piece should already be selected.
   *
   * @param x, x coordinate of the new space
   * @param y, y coordinate of the new space
   */
  @Override
  protected void makeMove(int x, int y) {
    myView.removePossibleMoves(myBoard.getValidMoves());
    myBoard.movePiece(x, y); // x,y are a highlighted cell of new piece position
    System.out.println(myBoard.getCurrentRow() + " " + myBoard.getCurrentCol());
    myView.updateCell(x, y,
        myBoard.getPieceAtSelect(myBoard.getCurrentRow(), myBoard.getCurrentCol()));
//        myView.updateCell(
//                myBoard.getCurrentRow(),
//                myBoard.getCurrentCol(),
//                myBoard.getPieceAt(myBoard.getCurrentRow(), myBoard.getCurrentCol()));
    setPieceChosen(false);
  }

  /**
   * TODO: What happens when we click on a cell (need to somehow implement this also for potential new pieces
   *
   * @param coordinate Overall coordinate
   */
  @Override
  public void onCellClick(Point coordinate) {
    int x = (int) coordinate.getX();
    int y = (int) coordinate.getY();

    if (deleteActive) {
      tryToDeletePiece(x, y);
    } else {
      tryToPlacePiece(x, y);
    }
  }

  private void tryToPlacePiece(int x, int y) {
    if (getPieceChosen() && isInValidMoves(x, y)) {
      makeMove(x, y);
    } else if (!getPieceChosen()) {
      myMessageBuilder.showMessage(SELECT_PIECE);
    } else {
      myMessageBuilder.showMessage(INVALID_MOVE); // TODO move to language resources
    }
  }

  private void tryToDeletePiece(int x, int y) {
    if (isInValidMoves(x, y)) {
      removePiece(x, y);
    } else {
      myMessageBuilder.showMessage(INVALID_MOVE);
    }
    deleteActive = false;
  }

  private void removePiece(int x, int y) {
    myView.removePossibleMoves(myBoard.getValidMoves());
    myBoard.deletePiece(x, y);
    myView.updateCell(x, y, null);
    setPieceChosen(false);
  }

  /**
   * What happens when the view clicks on one of the newPiece buttons. Main: Change which newPiece
   * is currently selected (implement similar to onCellClick for the first one).
   *
   * @param y, y coordinate of Selector Piece from that grid (specify this in View)
   */
  public void onNewPieceClick(int y) {
    updatePieceChosen(teamNum, y);
  }

  /**
   * Buttons at the bottom of the screen. TODO: Figure out if this includes saveGame. Maybe this is
   * where we put the new pieces?
   */
  @Override
  protected void getGameButtons() {
    myBoard = (GameAreaEditorBoard) getMyBoard();
    //int numTeams = 2;
    //Grid newPieceGrid = getNewPiecesBoard();
    String fileName = CSV_PATH + "PotentialPieces_board.csv";
    //myParser.readCSVFile(new File(fileName));
    try {
      myBoard.initializePotentialPieces(getNewPiecesBoard(fileName),
          PROPERTY_PATH + getGameClass().toLowerCase() + PROPERTIES);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("caught" + e.getMessage());
    }
    addButton("Pawn", e -> onNewPieceClick(0));
    addButton("Rook", e -> onNewPieceClick(1));
    addButton("Knight", e -> onNewPieceClick(2));
    addButton("Bishop", e -> onNewPieceClick(3));
    addButton("Queen", e -> onNewPieceClick(4));
    addButton("King", e -> onNewPieceClick(5));

    addButton("Trash", e -> toggleDeleteAction());

    addButton("NextTeam", e -> nextTeam());

    //TODO: Button(s) for the potential new pieces --> Call onNewPieceClick (see board clicking implementation in view)
    //Investigate: make new consumer setup
    //TODO: Trash --> Set trashActive, adjust other methods for this
//        addButton("Castling", e -> setCastling());
//        addButton("Check", e -> setCheck());
  }

  private void nextTeam() {
    teamNum ++;
    if(teamNum > MAX_ALLOWED_TEAMS){
      teamNum = 0;
    }
  }

  private void toggleDeleteAction() {
    if (deleteActive) {
      deleteActive = false;
      myView.removePossibleMoves(myBoard.getValidMoves());
      setPieceChosen(false);
    } else {
      deleteActive = true;
      myBoard.findFullSpaces();
      if (myBoard.getValidMoves().size() == 0) {
        myMessageBuilder.showMessage(NO_VALID_MOVES);
      }
      sendValidMoves();
    }
  }


  private Grid getNewPiecesBoard(String fileName) throws CsvValidationException, IOException {
    CSVParser newPieceParser = new CSVParser();
    newPieceParser.readCSVFile(new File(fileName));
    return newPieceParser.getInitialStates();
  }
//    @Override
//    public void sendValidMoves() {
//        List<Move> myMoves = myBoard.getValidMoves();
//        myView.showPossibleMoves(myMoves);
//    }
}
