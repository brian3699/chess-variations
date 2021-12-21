package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ooga.model.controller.MenuController;
import ooga.model.util.Grid;
import ooga.util.CSVParser;
import ooga.view.board.BoardDisplay;
import org.junit.jupiter.api.Test;
import org.testfx.matcher.base.NodeMatchers;
import util.DukeApplicationTest;

/**
 * @author Evelyn Cupil-Garcias
 *
 * Testing Class that tests functionality that is across all game types.
 */
public class GameViewTest extends DukeApplicationTest {

  private MenuController mainController;
  private BoardDisplay board;

  /**
   * Start method to run tests.
   * @param stage stage to display.
   */
  @Override
  public void start(Stage stage) {
    mainController = new MenuController(stage);
    mainController.startOptionsView();
  }

  /**
   * Asserts that when a game is started, the board grid exists.
   */
  @Test
  public void assertGameBoardExists() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    GridPane grid = lookup("#Board").query();
    assertTrue(grid.getChildren() != null);
  }

  /**
   * Check that the correct game was initialized, in this case Chess.
   */
  @Test
  public void correctChessGameInitialized() throws CsvValidationException, IOException {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    GridPane grid = lookup("#Board").query();
    populateBoard();
    System.out.println(grid.getColumnCount());
    assertEquals(grid.getColumnCount(), board.getBoardGrid().getColumnCount());
    assertEquals(grid.getRowCount(), board.getBoardGrid().getColumnCount());
  }

  private void populateBoard() throws CsvValidationException, IOException {
    File chess = new File("data/boards/Chess_board.csv");
    CSVParser parser = new CSVParser();
    board = new BoardDisplay(e -> {}, "Chess");
    parser.readCSVFile(chess);
    Grid boardGrid = parser.getInitialStates();
    board.setBoard(boardGrid.width(), boardGrid.length());
    for (int i = 0; i < boardGrid.width(); i++) {
      for (int j = 0; j < boardGrid.length(); j++) {
        if (boardGrid.getVal(i,j) == "") {
          board.drawBoard('E', i, j, 0);
        } else {
          Character tag = boardGrid.getVal(i,j).charAt(0);
          Character team = boardGrid.getVal(i,j).charAt(1);
          board.drawBoard(tag, i, j, Integer.parseInt(Character.toString(team)));
        }
      }
    }
  }

  /**
   * Test that shows invalid move detected.
   */
  @Test
  public void invalidMoveDetected() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    GridPane grid = lookup("#Board").query();
    clickOn(grid.getChildren().get(0));
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /**
   * Test that shows wrong team was clicked on to move a piece.
   */
  @Test
  public void wrongTeamDetected() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    GridPane grid = lookup("#Board").query();
    clickOn(grid.getChildren().get(14));
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /**
   * Test that shows that a valid piece was clicked.
   */
  @Test
  public void validPieceClicked() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    GridPane grid = lookup("#Board").query();
    clickOn(grid.getChildren().get(48));
    assertEquals(grid.getChildren().get(40).getStyle(), "-fx-background-color: #00FF00");
  }

  /**
   * Test that shows that a valid piece was clicked and moved.
   */
  @Test
  public void validPieceMoved() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    GridPane grid = lookup("#Board").query();
    HBox piece = (HBox) grid.getChildren().get(48);
    clickOn(grid.getChildren().get(48));
    clickOn(grid.getChildren().get(40));
    HBox newPiece = (HBox) grid.getChildren().get(40);
    Rectangle pieceImage = (Rectangle) piece.getChildren().get(0);
    Rectangle newPieceImage = (Rectangle) newPiece.getChildren().get(0);
    assertNotEquals(pieceImage.getFill(), newPieceImage.getFill());
  }

  /**
   * Entering a game then returning to home screen
   */
  @Test
  public void goingToHomeFromChessGame() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    Button home = lookup("#Home").query();
    clickOn(home);
    Button playCheckers = lookup("#PlayCheckers").query();
    assertTrue(playCheckers.isVisible());
  }

  /**
   * Saved Game appears and shows FileChooser as an option.
   */
  @Test
  public void startGameFromUpload() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button uploadSavedGame = lookup("#UploadSavedGame").query();
    clickOn(uploadSavedGame);
    assertTrue(uploadSavedGame.isVisible());
  }

  /**
   * Can save a game after moving pieces around the board.
   */
  @Test
  public void saveGame() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    GridPane grid = lookup("#Board").query();
    clickOn(grid.getChildren().get(51));
    clickOn(grid.getChildren().get(35));
    clickOn(grid.getChildren().get(12));
    clickOn(grid.getChildren().get(28));
    Button saveGame = lookup("#SaveGame").query();
    clickOn(saveGame);
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /**
   * Test that checks if Castling is possible.
   */
  @Test
  public void doCastling() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    Button castling = lookup("#Castling").query();
    clickOn(castling);
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /**
   * Test that checks if Pawn Promotion is possible.
   */
  @Test
  public void doPawnPromotion() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    Button pawnPromotion = lookup("#PawnPromotion").query();
    clickOn(pawnPromotion);
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /**
   * Test that checks if Pawn Promotion with Captured Pieces is possible.
   */
  @Test
  public void doRetrieveCapturedPromotion() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    Button pawnPromotion = lookup("#PawnPromotion2").query();
    clickOn(pawnPromotion);
    verifyThat("OK", NodeMatchers.isVisible());
  }

  /**
   * Test that applies cheat code.
   */
  @Test
  public void applyCheatCode() {
    Button playChess = lookup("#PlayChess").query();
    clickOn(playChess);
    Button newGame = lookup("#StartNewGame").query();
    clickOn(newGame);
    Button cheatCode = lookup("#ApplyCheat").query();
    clickOn(cheatCode);
    verifyThat("OK", NodeMatchers.isVisible());
  }

}
