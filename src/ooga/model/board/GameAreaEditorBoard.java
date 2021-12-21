package ooga.model.board;

import ooga.model.piece.PieceInterface;
import ooga.model.util.Grid;
import ooga.model.util.Move;

import javax.management.ReflectionException;
import java.util.ArrayList;
import java.util.List;

public class GameAreaEditorBoard extends Board {

  private int boardSize;
  private List<List<PieceInterface>> potentialNewPieces;

  public GameAreaEditorBoard() {
    super();
  }

  /**
   * Given a Grid object consisting of a board initial configuration csv file, initialize the
   * boardPieces with the proper pieces and their properties as well as the current active team
   * playing the game
   *
   * @param selectorGrid, blank board of the specified size
   */
  public void initializePotentialPieces(Grid selectorGrid, String piecePath)
      throws ReflectionException, ClassNotFoundException, NoSuchMethodException {
    potentialNewPieces = new ArrayList<List<PieceInterface>>();
    for (int i = 0; i < selectorGrid.length(); i++) {
      List<PieceInterface> rowPieces = new ArrayList<>();
      for (int j = 0; j < selectorGrid.width(); j++) {
        if (selectorGrid.getVal(i, j).equals("")) {
          rowPieces.add(null);
        } else {
          String nextPiece = selectorGrid.getVal(i, j);
          PieceInterface tempPiece = makePiece(nextPiece, piecePath);
          rowPieces.add(tempPiece);
        }
      }
      potentialNewPieces.add(rowPieces);
    }
  }

  /**
   * Finds all empty spaces on the board that the new piece can be placed into
   *
   * @param selectorRow
   * @param selectorCol
   */
  @Override
  public void findValidMoves(int selectorRow, int selectorCol) {
    setCurrentPiece(getPieceAtSelect(selectorRow, selectorCol));
    setCurrentRow(selectorRow);
    setCurrentRow(selectorCol);
    boardSize = getBoardLength();
    for (int newRow = 0; newRow < boardSize; newRow++) {
      for (int newCol = 0; newCol < boardSize; newCol++) {
        if (validateGeneralMove(newRow, newCol) && getPieceAt(newRow, newCol) == null) {
          Move newMove = new Move(newRow, newCol);
          addValidMove(newMove);
        }
      }
    }
  }

  public PieceInterface getPieceAtSelect(int selectorRow, int selectorCol) {
    return potentialNewPieces.get(selectorRow).get(selectorCol);
  }

  /**
   * Finds spaces on the board that are full with a piece Used to delete pieces
   */
  public void findFullSpaces() {
    boardSize = getBoardLength();
    for (int newRow = 0; newRow < boardSize; newRow++) {
      for (int newCol = 0; newCol < boardSize; newCol++) {
        if (validateGeneralMove(newRow, newCol) && getPieceAt(newRow, newCol) != null) {
          Move newMove = new Move(newRow, newCol);
          addValidMove(newMove);
        }
      }
    }
  }

  /**
   * Removes a piece from the board at given coordinates
   *
   * @param x
   * @param y
   */
  public void deletePiece(int x, int y) {
    removePiece(x, y);
    clearPossibleMoves();
  }

  @Override
  public void movePiece(int x, int y) {
    createPiece(x, y);
    clearPossibleMoves();
  }

  /**
   * Does not calculate a winner since there are no winners in the editor
   */
  @Override
  public void hasWon() {
  }

  @Override
  protected boolean validateNewPosition(int row, int col) {
    return false;
  }

//  @Override
//  protected boolean validBounds(int row, int col) {
//    return row < 0 || row >= getBoardLength() || col < 0 || col >= getBoardWidth();
//  }
}
