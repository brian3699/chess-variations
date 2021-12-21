package ooga.view.board;

import javafx.scene.Node;
import ooga.model.util.Move;

import java.util.List;

/**
 * Creates the board of the game
 */
public interface BoardDisplayInterface {

  /**
   * Updates a piece on the board.
   *
   * @param x    row number of piece.
   * @param y    column number of piece.
   * @param tag  piece type.
   * @param team piece's team.
   */
  void update(int x, int y, char tag, int team);

  /**
   * Getter method of boardGrid.
   *
   * @return boardGrid.
   */
  Node getBoardGrid();
  // BoardGrid is returned as a Node to prevent user from editing the BoardGrid directly
  // Any changes to the boardGrid can only be made using public methods provided in BoardDisplayInterface

  /**
   * Draw's a piece on the board.
   *
   * @param tag    piece type.
   * @param rowNum row column of piece.
   * @param colNum column number of piece.
   * @param team   team number of piece.
   */
  void drawBoard(char tag, int rowNum, int colNum, int team);

  /**
   * set grid lines
   */
  void setGridLines();

  /**
   * Highlight possible moves in green.
   *
   * @param possibleMoves List of points with possible moves.
   */
  void showPossibleMoves(List<Move> possibleMoves);

  /**
   * Removes possible moves that are in green.
   *
   * @param possibleMoves List of points with possible moves.
   */
  void removePossibleMoves(List<Move> possibleMoves);

  /**
   * Initializes board with grid lines.
   */
  void setBoard(int row, int col);

  /**
   * set color of the board
   *
   * @param colorHex color
   */
  void setColor(String colorHex);

}
