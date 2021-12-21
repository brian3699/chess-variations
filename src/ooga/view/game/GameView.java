package ooga.view.game;

import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ooga.model.util.Move;
import ooga.model.piece.PieceInterface;
import ooga.view.board.BoardDisplay;
import ooga.view.board.BoardDisplayInterface;
import ooga.view.components.ButtonFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * Class that implements GameViewInterface of the Game. Creates the scene of the game
 *
 * @author Evelyn Cupil-Garcia, Young Jun
 */
public class GameView implements GameViewInterface {

  private static final String DEFAULT_RESOURCE_PACKAGE = "ooga.view.resources.CSS.";
  private static final String FOUR_PLAYER_CHESS = "FourPlayerChess";
  private static final String DEFAULT_STYLESHEET =
      "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/") + "Default.css";
  private static final String DEFAULT_VALUES_RESOURCES_PATH = "ooga.view.resources.pieceDirectory.magicValues";
  private static final String PLAYER = "player";
  private static final String EMPTY = "Empty";
  private static final String ZERO = "EmptyTeam";
  private static final String HEADER = "Header";
  private static final String POINTS = "Points";
  private static final String TITLE_ID = "Title";
  private static final String[] HEADER_FORMAT = new String[]{" [", " : ", "] [", "]"};

  private Scene gameViewScene;
  private VBox root;
  private int currentPlayer;
  private BoardDisplayInterface boardDisplay;
  private Consumer clickConsumer;
  private ButtonFactory buttonGenerator;
  private String gameType;
  private ResourceBundle myValues;
  private ResourceBundle language;
  private int[] scoreBoard;
  private VBox gameHeader;


  /**
   * Constructor for GameView.
   *
   * @param buttonMap to draw associate buttons.
   */
  public GameView(String gameType, Map<String, EventHandler> buttonMap, Consumer clickConsumer,
      ResourceBundle languageResource) {
    this.clickConsumer = clickConsumer;
    this.language = languageResource;
    buttonGenerator = new ButtonFactory(languageResource);
    currentPlayer = 1;
    scoreBoard = new int[]{0, 0, 0, 0};
    this.gameType = gameType;
    initializeRoot(buttonMap);
    gameViewScene = new Scene(root);
    gameViewScene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource(DEFAULT_STYLESHEET)).toExternalForm());
    myValues = ResourceBundle.getBundle(DEFAULT_VALUES_RESOURCES_PATH);
  }

  private void initializeRoot(Map<String, EventHandler> buttonMap) {
    root = new VBox();
    boardDisplay = new BoardDisplay(clickConsumer, gameType);
    gameHeader = createGameHeader();
    HBox buttonBox = buttonGenerator.createMultipleButtons(buttonMap);
    root.getChildren().addAll(gameHeader, boardDisplay.getBoardGrid(), buttonBox);
  }


  private VBox createGameHeader() {
    VBox ret = new VBox();
    Text title = new Text(language.getString(gameType));
    ret.setId(TITLE_ID);
    Text header = new Text(
        language.getString(HEADER) + language.getString(gameType + PLAYER + currentPlayer));
    if (gameType.equals(FOUR_PLAYER_CHESS)) { // Create Header and Points record for 4 players
      ret.getChildren()
          .addAll(title, header, new Text(language.getString(POINTS + 1) + scoreBoard[0]
              + language.getString(POINTS + 2) + scoreBoard[1] + language.getString(POINTS + 3) +
              scoreBoard[2] + language.getString(POINTS + 4) + scoreBoard[3] + HEADER_FORMAT[3]));
    } else { // Create Header and Points record for 2 players
      ret.getChildren().addAll(title, header,
          new Text(language.getString(POINTS) + HEADER_FORMAT[0] + language.getString(
              gameType + PLAYER + 1)
              + HEADER_FORMAT[1] + scoreBoard[0] + HEADER_FORMAT[2] +
              language.getString(gameType + PLAYER + 2) + HEADER_FORMAT[1] + scoreBoard[1]
              + HEADER_FORMAT[3]));
    }
    return ret;
  }

  /**
   * Method that draws a piece on the board.
   *
   * @param piece  that gives information on what type of piece it is, and it's associate team.
   * @param rowNum row number of the piece.
   * @param colNum column number of the piece.
   */
  public void initializePiece(PieceInterface piece, int rowNum, int colNum) {
    if (piece == null) {
      boardDisplay.drawBoard(
          (myValues.getString(EMPTY).charAt(Integer.parseInt(myValues.getString(ZERO)))),
          rowNum, colNum, Integer.parseInt(myValues.getString(ZERO)));
    } else {
      boardDisplay.drawBoard(piece.getKey(), rowNum, colNum, piece.getTeam());
    }
  }

  /**
   * Updates the piece at its specific coordinate.
   *
   * @param x     row number of the piece.
   * @param y     column number of the piece.
   * @param piece piece's information.
   */
  public void updateCell(int x, int y, PieceInterface piece) {
    if (piece == null) {
      boardDisplay.update(x, y,
          (myValues.getString(EMPTY).charAt(Integer.parseInt(myValues.getString(ZERO)))),
          Integer.parseInt(myValues.getString(ZERO)));
    } else {
      boardDisplay.update(x, y, piece.getKey(), piece.getTeam());
    }
  }

  /**
   * Paint possible move cells in green.
   *
   * @param possibleMoves List of points of possible moves.
   */
  public void showPossibleMoves(List<Move> possibleMoves) {
    boardDisplay.showPossibleMoves(possibleMoves);
  }

  /**
   * Remove possible moves painted in green
   *
   * @param possibleMoves list of points of possible moves
   */
  public void removePossibleMoves(List<Move> possibleMoves) {
    boardDisplay.removePossibleMoves(possibleMoves);
  }

  /**
   * Initialize and set board
   *
   * @param row row number
   * @param col column number
   */
  public void initializeBoard(int row, int col) {
    boardDisplay.setBoard(row, col);
  }

  /**
   * set grid lines of the board
   */
  public void initializeGridLines() {
    boardDisplay.setGridLines();
  }

  /**
   * Update current player displayed in GameView.
   */
  public void setActiveTeam(int player) {
    currentPlayer = player;
    updateHeader();
  }

  /**
   * Update score of players displayed in GameView.
   *
   * @param activeTeam      the active team.
   * @param activeTeamScore the active team's score.
   */
  public void updateScore(int activeTeam, int activeTeamScore) {
    scoreBoard[activeTeam - 1] = activeTeamScore;
    updateHeader();
  }

  private void updateHeader() {
    root.getChildren().remove(0);
    gameHeader = createGameHeader();
    root.getChildren().add(0, gameHeader);
  }

  /**
   * Getter for the GameView scene.
   *
   * @return gameViewScene.
   */
  @Override
  public Scene getScene() {
    gameViewScene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource(DEFAULT_STYLESHEET)).toExternalForm());
    return gameViewScene;
  }

}
