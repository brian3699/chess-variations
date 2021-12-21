package ooga.model.board;

import java.util.List;
import ooga.model.piece.CheckersPiece;
import ooga.model.piece.PieceInterface;
import ooga.model.util.Move;

public class CheckersBoard extends Board {

  private static final int ZERO_SPACE = 0;
  private static final int HALF = 2;
  private int rowOrientation;
  private int colOrientation;
  private boolean turnOver;
  private boolean jumped;

  public CheckersBoard() {
    super();
    rowOrientation = ZERO_SPACE;
    colOrientation = ZERO_SPACE;
    turnOver = false;
    jumped = false;
  }


  /**
   * This method finds the valid checkers move for one iteration around the current position of the
   * chosen piece
   *
   * @param row the row position of the current piece
   * @param col the column position of the current piece
   */
  @Override
  public void findValidMoves(int row, int col) {
    clearPossibleMoves();
    jumped = false;
    turnOver = false;
    setCurrentRow(row);
    setCurrentCol(col);
    setCurrentPiece(getPieceAt(row, col));
    List<Move> pieceCaptureRules = ((CheckersPiece) getCurrentPiece()).getCaptureMoves();
    List<Move> pieceRules = getCurrentPiece().getPossibleMoves();
    validateAndAddMove(pieceCaptureRules, getCurrentRow(), getCurrentCol());
    validateAndAddMove(pieceRules, getCurrentRow(), getCurrentCol());
  }


  private void validateAndAddMove(List<Move> pieceRules, int r, int c) {
    for (Move captureMove : pieceRules) {
      int startRow = r;
      int startCol = c;
      startRow += captureMove.deltaRow();
      startCol += captureMove.deltaCol();
      rowOrientation = (r - startRow) / HALF; // to find jumped piece
      colOrientation = (c - startCol) / HALF; // to find jumped piece
      validateMove(startRow, startCol);
    }
  }

  private void validateMove(
      int startRow, int startCol) {
    if (validateGeneralMove(startRow, startCol)) {
      if (Math.abs(colOrientation) > ZERO_SPACE) // checks to see if it's a capture move
      {
        validateCMove(startRow, startCol);
      } else {
        addValidMove(new Move(startRow, startCol));
      }
    }
  }


  private void validateCMove(int startRow, int startCol) {
    PieceInterface jumpedPiece = getPieceAt(startRow + rowOrientation, startCol + colOrientation);
    //System.out.println((startRow + rowOrientation)+" "+(startCol + colOrientation));
    if (jumpedPiece != null && jumpedPiece.getTeam() != getCurrentPiece().getTeam()) {
      addValidMove(new Move(startRow, startCol));
    }
  }

  /**
   * moves the current piece and removes any jumped pieces if applicable
   *
   * @param r the new row position of the piece
   * @param c the new col position of the piece
   */
  @Override
  public void movePiece(int r, int c) {
    updateTeamVals(r + ((getCurrentRow() - r) / HALF), c + ((getCurrentCol() - c) / HALF));
    updatePieces(r, c);
    if (Math.abs((r - getCurrentRow()) / HALF) > ZERO_SPACE) {
      pieceJumped(r + ((getCurrentRow() - r) / HALF), c + ((getCurrentCol() - c) / HALF));
      jumped = true;
    }
    hasWon();
    clearPossibleMoves();
  }

  /**
   * sets boolean to determine if the turn is over
   *
   * @param r the row of the current cell
   * @param c the column of the current cell
   */
  public void setIfTurnOver(int r, int c) {
    if (jumped) {
      validateAndAddMove(((CheckersPiece) getPieceAt(r, c)).getCaptureMoves(), r, c);
      if (getValidMoves().size() == 0) {
        turnOver = true;
      }
    }
    if (!jumped) {
      turnOver = true;
    }
  }

  /**
   * checks if the game has been won and sets the team that has won the game
   */
  @Override
  protected void hasWon() {
    for (Integer teamNum : getTeams().keySet()) {
      int count = 0;
      for (int key : getTeam(teamNum).getCaptured().keySet()) {
        count += getTeam(teamNum).getCaptured().get(key).size();
        if (count >= getTeam(key).getSizeOfTeam()) {
          setTeamWon(teamNum);
          break;
        }
      }

    }
  }

  @Override
  protected boolean validateNewPosition(int row, int col) {
    return getPieceAt(row, col) != null;
  }

  public void setTurnOver(boolean t) {
    turnOver = t;
  }

  public boolean getTurnOver() {
    return turnOver;
  }

  public boolean getJumped() {
    return jumped;
  }

}
