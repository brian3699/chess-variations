package ooga.model.util;

import java.util.List;
import ooga.model.piece.PieceInterface;

public class Layout {

  private int r;
  private int c;
  private TeamStatus[][] setup;

  /**
   * creates a layout based on row/col values for piece to be moved and a 2D list of all
   * boardPieces
   *
   * @param currentPieceRow row of piece to be moved
   * @param currentPieceCol col of piece to be moved
   * @param boardPieces     list of all pieces on board
   */
  public Layout(int currentPieceRow, int currentPieceCol, List<List<PieceInterface>> boardPieces) {
    r = currentPieceRow;
    c = currentPieceCol;
    setup = new TeamStatus[boardPieces.size()][boardPieces.get(0).size()];
    PieceInterface currentPiece = boardPieces.get(currentPieceRow).get(currentPieceCol);
    for (int i = 0; i < boardPieces.size(); i++) {
      for (int j = 0; j < boardPieces.get(0).size(); j++) {
        PieceInterface p = boardPieces.get(i).get(j);
        if (p == null) {
          setup[i][j] = TeamStatus.EMPTY;
        } else if (currentPiece.getTeam() == p.getTeam()) {
          setup[i][j] = TeamStatus.OWN;
        } else {
          setup[i][j] = TeamStatus.OTHER;
        }
      }
    }
  }

  /**
   * gets TeamStatus at some space on board
   *
   * @param r row value
   * @param c col value
   * @return Team status at r/c
   */
  public TeamStatus get(int r, int c) {
    return setup[r][c];
  }

  /**
   * @return row of piece to be moved
   */
  public int getR() {
    return r;
  }

  /**
   * @return col of piece to be moved
   */
  public int getC() {
    return c;
  }
}
