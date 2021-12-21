package ooga.view.game;

import javafx.scene.Scene;
import ooga.model.piece.PieceInterface;
import ooga.model.util.Move;

import java.util.List;

/**
 * Creates View interface.
 */
public interface GameViewInterface {

  /**
   * Method that draws a piece on the board.
   *
   * @param piece  that gives information on what type of piece it is, and it's associate team.
   * @param rowNum row number of the piece.
   * @param colNum column number of the piece.
   */
  void initializePiece(PieceInterface piece, int rowNum, int colNum);

  /**
   * Updates the piece at its specific coordinate.
   *
   * @param x     row number of the piece.
   * @param y     column number of the piece.
   * @param piece piece's information.
   */
  void updateCell(int x, int y, PieceInterface piece);

  /**
   * Paint possible move cells in green.
   *
   * @param possibleMoves List of points of possible moves.
   */
  void showPossibleMoves(List<Move> possibleMoves);

  /**
   * Remove possible moves painted in green
   *
   * @param possibleMoves list of points of possible moves
   */
  void removePossibleMoves(List<Move> possibleMoves);

  /**
   * Initialize and set board
   *
   * @param row row number
   * @param col column number
   */
  void initializeBoard(int row, int col);

  /**
   * set grid lines of the board
   */
  void initializeGridLines();

  /**
   * Update current player displayed in GameView.
   */
  void setActiveTeam(int player);

  /**
   * Update score of players displayed in GameView.
   *
   * @param activeTeam      the active team.
   * @param activeTeamScore the active team's score.
   */
  void updateScore(int activeTeam, int activeTeamScore);

  /**
   * Getter for the GameView scene.
   *
   * @return gameViewScene.
   */
  Scene getScene();

}
