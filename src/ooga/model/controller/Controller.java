package ooga.model.controller;

import com.opencsv.exceptions.CsvValidationException;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.event.EventHandler;
import javafx.stage.Stage;

import javax.management.ReflectionException;

import ooga.model.board.BoardInterface;

import ooga.model.piece.Piece;
import ooga.model.piece.PieceInterface;
import ooga.model.util.Grid;
import ooga.model.util.Move;
import ooga.util.CSVParser;
import ooga.util.CSVParserInterface;
import ooga.view.components.MessageBuilder;
import ooga.view.components.UserInputBuilder;
import ooga.view.game.GameView;
import ooga.view.game.GameViewInterface;

public abstract class Controller {
  public static final String KING = "King";
  public static final String QUEEN = "Queen";
  public static final String ROOK = "Rook";
  public static final String BISHOP = "Bishop";
  public static final String PAWN = "Pawn";
  public static final String KNIGHT = "Knight";
  public static final char KING_KEY = 'K';
  public static final char QUEEN_KEY = 'Q';
  public static final char ROOK_KEY = 'R';
  public static final char BISHOP_KEY = 'B';
  public static final char PAWN_KEY = 'P';
  public static final char KNIGHT_KEY = 'N';

  private static final String EMPTY_SPACE = "-";
  private static final String MODEL_PATH = "ooga.model.board.";
  private static final String BOARD = "Board";
  private static final String DEFAULT_CSS = "ooga.view.resources.CSS.Default.css";
  private static final String SAVED_FILE_PATH = "data/savedFile/";
  private static final String PROPERTY_PATH = "ooga/model/resources/";
  private static final String PROPERTIES = ".properties";
  private static final String HOME = "Home";
  private static final String FORMAT = "HH:mm:ss";

  //ERROR CODES
  protected static final String INCORRECT_TEAM = "IncorrectTeam";
  protected static final String SELECT_PIECE = "SelectAPieceToMove";
  protected static final String INVALID_MOVE = "InvalidMove";
  protected static final String NO_VALID_MOVES = "NoValidMovesForThisPiece";

  private static final String FILE_SUCCESS = "FileSavedSuccessfully";
  private static final String FILE_ERROR = "FileSaveError";
  private static final String PAWN_PROMOTION = "PawnPromotion";
  private static final String PAWN_PROMOTION_2 = "PawnPromotion2";
  private static final String SAVE_GAME = "SaveGame";
  private static final int NO_TEAM = -1;
  private static final String INVALID_FILE = "File Not Found";
  private static final String BOARDS_PATH = "data/boards/cheatKeys/";
  private static final String ENTER_CODE = "EnterCode";
  private static final String BOARD_PATH = "_board.csv";
  private static final String APPLY_CHEAT = "ApplyCheat";


  private BoardInterface myBoard;
  private CSVParserInterface myParser;
  private GameViewInterface myView;
  private boolean pieceChosen;
  private EventHandler mainStarter;
  private Map<String, EventHandler> buttonMap;
  private EventHandler home;
  private ResourceBundle languageResource;
  private MessageBuilder messageBuilder;
  private UserInputBuilder userInputBuilder;
  private Stage myStage;
  private File originalFile;
  private String gameType;


  public Controller(EventHandler event, String gameType, ResourceBundle languageResource, File file)
      throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException,
      NoSuchMethodException {
    home = event;
    myBoard = createBoard();
    initializeMap();
    this.languageResource = languageResource;
    messageBuilder = new MessageBuilder(languageResource);
    userInputBuilder = new UserInputBuilder(languageResource);
    myView = new GameView(gameType, buttonMap, e -> onCellClick((Point) e), languageResource); //myView will be initialize
    myParser = new CSVParser();
    mainStarter = event;
    originalFile = file;
    this.gameType = gameType;
    setupGame(file);
  }

  private BoardInterface createBoard()
      throws ReflectionException {
    try {
      Class<?> game = Class.forName(MODEL_PATH + getGameClass() + BOARD);
      Constructor constructor = game.getDeclaredConstructor();
      BoardInterface component = (BoardInterface) constructor.newInstance();
      return component;

    } catch (RuntimeException
        | ClassNotFoundException
        | NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  }

  protected abstract String getGameClass();

  protected abstract void makeMove(int r, int c);

  protected abstract void onCellClick(Point coordinate);


  private void setupGame(File file)
      throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException,
      NoSuchMethodException {
    myParser.readCSVFile(file);
    myBoard.initialize(myParser.getInitialStates(),
        PROPERTY_PATH + getGameClass().toLowerCase() + PROPERTIES);
    initializeBoardView();
    pieceChosen = false;
  }


  private void initializeBoardView() {
    myView.initializeBoard(myParser.getDimensions()[0], myParser.getDimensions()[1]);
    for (int i = 0; i < myParser.getDimensions()[0]; i++) {
      for (int j = 0; j < myParser.getDimensions()[1]; j++) {
        myView.initializePiece(myBoard.getPieceAt(i, j), i, j);
      }
    }
    myView.initializeGridLines();
  }

  protected boolean hasPieceAt(int x, int y) {
    return myBoard.getPieceAt(x, y) != null;
  }

  protected boolean isCorrectTeam(int x, int y) {
    return myBoard.getPieceAt(x, y).getTeam() == myBoard.getActiveTeam().getTeamNum();
  }

  protected void updatePieceChosen(int x, int y) {
    myView.removePossibleMoves(myBoard.getValidMoves());
    myBoard.findValidMoves(x, y);
    if (myBoard.getValidMoves().size() == 0) {
      messageBuilder.showMessage(NO_VALID_MOVES);
    }
    sendValidMoves();
    pieceChosen = true;
  }

  /**
   * method checks if the value of team won is assigned to a valid team number and if it is the game
   * announces that team has won
   */
  protected void checkIfTeamWon() {
    if (myBoard.getHasWon() != NO_TEAM) {
      Map<String, EventHandler> mapButtons = new HashMap<>();
      mapButtons.put(HOME, home);
      myStage.close();
      // TODO: add to resource file as key
      messageBuilder.showPopupScene("GameOver", "PlayerWon" + myBoard.getHasWon(), mapButtons);
    }
  }


  protected boolean isInValidMoves(int x, int y) {
    List<Move> moves = myBoard.getValidMoves();
    if (moves != null) {
      for (Move move : moves) {
        if (move.deltaRow() == x && move.deltaCol() == y) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * apply key letter to jump ahead in the game and add a "cheat" to the game
   */
  protected void applyCheat()
      throws IOException, ReflectionException, CsvValidationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    String cheat = userInputBuilder.getTextInput(ENTER_CODE);
    if(cheat!=null){
      File file = new File((BOARDS_PATH + cheat.toUpperCase() + gameType+BOARD_PATH));
      Stage stage = new Stage();
      MenuController controller = new MenuController(stage);
      controller.startGame(getGameClass(), file);
      setupGame(file);
      startGame(stage);
    }
  }

  /**
   * Upon the click of Pawn Promotion button in the view, this method handles whether or not pawn promotion can happen.
   * If it is possible, it calls the front end method that allows the user to select from the 4 options of promotion
   * This is the first pawning option rule
   */
  protected void setPawnPromotion() {
    if (!myBoard.checkPawnPromotion()) {
      messageBuilder.showMessage(INVALID_MOVE);
    } else {
      ArrayList<String> promotionList = new ArrayList<>(
          Arrays.asList(BISHOP, KNIGHT, ROOK, QUEEN));
      myBoard.promotePawn(getPieceKey(userInputBuilder.getDialogInput(promotionList.get(0), promotionList, languageResource.getString(PAWN_PROMOTION_2))));
      myView.initializePiece(myBoard.getPawnPromotionPiece(), myBoard.getPieceRow(myBoard.getPawnPromotionPiece()), myBoard.getPieceCol(myBoard.getPawnPromotionPiece()));
      myBoard.setActiveTeam();
      myView.setActiveTeam(myBoard.getActiveTeam().getTeamNum());
      setPieceChosen(false);
    }
  }

  /**
   * Upon the click of Pawn Promotion button in the view, this method handles whether or not pawn promotion can happen.
   * If it is possible, it calls the front end method that allows the user to select to promote a pawn from a piece
   * that another team has captured
   * This is the second pawning option rule
   */
  protected void setPawnPromotion2() {
    if (!myBoard.checkPawnPromotion() && myBoard.getCapturedPieces()!=null) {
      messageBuilder.showMessage(INVALID_MOVE);
    } else {
      ArrayList<String> promotionList = myBoard.getCapturedPieces();
      myBoard.promotePawn(getPieceKey(userInputBuilder.getDialogInput(promotionList.get(0), promotionList, languageResource.getString(PAWN_PROMOTION_2))));
      myBoard.setActiveTeam();
      myView.setActiveTeam(myBoard.getActiveTeam().getTeamNum());
      setPieceChosen(false);
    }
  }

  /**
   * Sends the View a list of the valid moves a piece can move to so that it can highlight those
   * pieces so that the user can view the valid moves
   */
  protected void sendValidMoves() {
    List<Move> myMoves = myBoard.getValidMoves();
    myView.showPossibleMoves(myMoves);
  }

  /**
   * start game in the front end
   *
   * @param stage is passed in from the menu page
   */
  public void startGame(Stage stage) {
    myStage = stage;
    stage.setScene(myView.getScene());
    stage.setFullScreen(true);
    stage.show();
  }

  /**
   * close current game
   */
  public void closeGame() {
    myStage.close();
  }


  /**
   * save the current game
   */
  private void saveFile() {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
      String path = SAVED_FILE_PATH + formatter.format(LocalTime.now());
      String[][] saveGrid =
          new String[myParser.getInitialStates().width()][myParser.getInitialStates().length()];

      for (int i = 0; i < saveGrid.length; i++) {
        for (int j = 0; j < saveGrid[0].length; j++) {
          PieceInterface tempPiece = myBoard.getPieceAt(i, j);
          String val;
          if (tempPiece != null) {
            String teamNum = String.valueOf(tempPiece.getTeam());
            String pieceKey = String.valueOf(tempPiece.getKey());
            val = pieceKey + teamNum;
          } else {
            val = EMPTY_SPACE;
          }
          saveGrid[i][j] = val;
        }
      }
      Grid saveFile = new Grid(saveGrid);
      myParser.saveGame(path, saveFile);
      messageBuilder.showMessage(FILE_SUCCESS);
    } catch (Exception e) {
      messageBuilder.showMessage(FILE_ERROR);
    }
  }

  protected BoardInterface getMyBoard() {
    return myBoard;
  }

  protected GameViewInterface getView() {
    return myView;
  }

  protected boolean getPieceChosen() {
    return pieceChosen;
  }

  protected void setPieceChosen(boolean chosen) {
    pieceChosen = chosen;
  }

  protected abstract void getGameButtons();

  private void initializeMap() {
    buttonMap = new LinkedHashMap<>();
    buttonMap.put(HOME, e -> closeGame());
    buttonMap.put(PAWN_PROMOTION, e -> setPawnPromotion());
    buttonMap.put(PAWN_PROMOTION_2, e -> setPawnPromotion2());
    buttonMap.put(SAVE_GAME, e -> saveFile());
    buttonMap.put(APPLY_CHEAT, e -> {
      try {
        applyCheat();
      } catch (IOException | ReflectionException | CsvValidationException | ClassNotFoundException | NoSuchMethodException|InvocationTargetException|InstantiationException|IllegalAccessException ex) {
        messageBuilder.showMessage(FILE_ERROR);

      }
    });
    getGameButtons();
  }

  protected void addButton(String key, EventHandler value) {
    buttonMap.put(key, value);
  }

  protected char getPieceKey(String pieceName){
    switch(pieceName){
      case KNIGHT: return KNIGHT_KEY;
      case KING: return KING_KEY;
      case QUEEN: return QUEEN_KEY;
      case ROOK: return ROOK_KEY;
      case BISHOP: return BISHOP_KEY;
      case PAWN: return PAWN_KEY;
      default: return ' ';
    }
  }

  public String getGameType(){
    return gameType;
  }

}
