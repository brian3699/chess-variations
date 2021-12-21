package ooga.model.board;

import java.util.ArrayList;
import java.util.List;
import ooga.model.piece.DynamicPiece;
import ooga.model.piece.KingPiece;
import ooga.model.piece.PawnPiece;
import ooga.model.piece.PieceInterface;
import ooga.model.team.TeamInterface;
import ooga.model.util.Direction;
import ooga.model.util.Layout;
import ooga.model.util.Move;

import javax.management.ReflectionException;

public class ChessBoard extends Board {

  private static final String PIECE_PROPERTY_PATH = "ooga/model/resources/chess.properties";
  private List<Integer> iterations;

  public ChessBoard() {
    super();
  }

  /**g
   * Upon a user's click of a board piece, this method analyzes the piece at the given x,y location
   * on the board and makes a list of all possible moves the user can move the piece to
   *
   * @param startRow the r value of the board piece being analyzed
   * @param startCol the c value of the board piece being analyzed
   */
  // TODO: make this part of board and make the submethods called unique to reduce repeated code
  @Override
  public void findValidMoves(int startRow, int startCol) {
    clearPossibleMoves();
    updateCurrentPiece(startRow, startCol);
    for(Move m : getAllValidMovesFor(startRow, startCol)){
      addValidMove(m);
    }
  }

  private List<Move> getAllValidMovesFor(int startRow, int startCol){
    List<Move> moves = new ArrayList<>();
    List<Move> pieceRules = getPieceAt(startRow, startCol).getPossibleMoves();
    int iterations = getPieceAt(startRow, startCol).getMoveIterations() == ZERO ? SINGLE_ITER : getBoardLength();
    Layout currentLayout = new Layout(startRow, startCol, getBoardPieces());
    for (Move rule : pieceRules) {
      for (int i = 1; i <= iterations; i++) {
        int moveRow = startRow + (i * rule.deltaRow());
        int moveCol = startCol + (i * rule.deltaCol());
        if (validBounds(moveRow, moveCol) && getPieceAt(startRow, startCol).isValidMove(moveRow, moveCol,
            currentLayout)) {
          moves.add(new Move(moveRow, moveCol));
          if (getPieceAt(moveRow, moveCol) != null) {
            break;
          }
        } else {
          break;
        }
      }
    }
    return moves;
  }

  private PieceInterface updateCurrentPiece(int r, int c) {
    setCurrentRow(r);
    setCurrentCol(c);
    setCurrentPiece(getPieceAt(r, c));
    return getCurrentPiece();
  }


  @Deprecated
  /**
   * this functionality is now included in Piece classes
   */
  protected boolean validateNewPosition(int row, int col) {
    return getPieceAt(row, col) != null
        && getPieceAt(row, col).getTeam() == getCurrentPiece().getTeam();
  }

  @Deprecated
  /**
   * this functionality has been moved to the PawnPiece class for a more closed implementation
   */
  private boolean validatePawnMove(Move possibleMove) {
    if (getCurrentPiece() instanceof PawnPiece) {
      int r = possibleMove.deltaRow();
      int c = possibleMove.deltaCol();
      PieceInterface targetPiece = getPieceAt(r, c);
      if (targetPiece == null && !isMoveForward(getCurrentPiece(), r, c)) {
        return false;
      }
      if (targetPiece != null && isMoveForward(getCurrentPiece(), r, c)) {
        return false;
      }
      if (isMoveForward(getCurrentPiece(), r, c) && Math.abs(getCurrentRow() - r) == 2
          && getPieceAt((getCurrentRow() + r) / 2, c) != null) {
        return false;
      }
      if (isMoveForward(getCurrentPiece(), r, c) && Math.abs(getCurrentCol() - c) == 2
          && getPieceAt(r, (getCurrentCol() + c) / 2) != null) {
        return false;
      }

    }
    return true;
  }

  @Deprecated
  /**
   * this functionality is moved to the pawn class
   */
  private boolean isMoveForward(PieceInterface p, int r, int c) {
    Direction pieceDirection = p.findDirection();
    Boolean isVertical = pieceDirection == Direction.UP || pieceDirection == Direction.DOWN;
    if (isVertical) {
      return c == getCurrentCol();
    } else {
      return r == getCurrentRow();
    }
  }

  /**
   * Given the r,c coordinates of the piece currently being moved, this method executes the move by
   * removing the current piece at that location, setting the piece as captured if necessary, and
   * moving in the new piece at that location
   *
   * @param r the original row of the piece
   * @param c the original y coordinate of the piece
   */
  @Override
  public void movePiece(int r, int c) {
    updateTeamVals(r, c);
    updatePieces(r, c);
    if (getCurrentPiece() instanceof DynamicPiece) {
      ((DynamicPiece) getCurrentPiece()).moved();
    }
    hasWon();
    clearPossibleMoves();
  }

  @Override
  protected void hasWon() {
    {
      // loop through all teams
      for (Integer teamNum : getTeams().keySet()) {
        // get the list of pieces that the team has capture
        for (Integer key : getTeam(teamNum).getCaptured().keySet()) {
          // check if the team has captured the king
          List<PieceInterface> tempPieces = getTeams().get(teamNum).getCaptured().get(key);
          for (PieceInterface p : tempPieces) {
            // if the king piece is found in the list of captured then that team has won
            if (p instanceof KingPiece) {
              setTeamWon(teamNum);
              return;
            }
          }
        }
      }
    }
  }

  /**
   * Method that loops through non-active teams, checks if they have any moves that contain
   * the current active team's king
   * @return whether the current active team's king is in check
   */
  public boolean checkForCheck() {
    List<Integer> teamNums = getTeamNumbers();
    for(int i = 1; i <= teamNums.size(); i++){
      for(int j = 1; j <= teamNums.size(); j++){
        if (i != j && otherTeamContainsMyKing(i, j)) {
          setKingCheck(true, j);
          return true;
        }
      }
      setKingCheck(false, i);
    }
    return false;
  }

  protected void setKingCheck(boolean inCheck, int teamNum){
    for (PieceInterface activeTeamPiece : getTeam(teamNum).getActive()) {
      if (activeTeamPiece.getKey() == KING_KEY) {
        if (inCheck) {
          ((KingPiece) activeTeamPiece).setInCheck();
        } else {
          ((KingPiece) activeTeamPiece).setNotInCheck();
        }
      }
    }
  }

  /**
   * Method that finds the current active team's king's coordinates, then loops through all of teamNum's
   * active pieces and calls a method that checks if any of their moves contains the current active king's
   * coordinates
   * @param myTeamNum the team whose moves we are checking to see if they contain the active King
   * @return whether teamNum as the active team's king in check
   */
  private boolean otherTeamContainsMyKing(int myTeamNum, int otherTeamNum) {
    int myKingRow = -1;
    int myKingCol = -1;
    for(PieceInterface p: getTeams().get(myTeamNum).getActive()){
      if(p.getKey() == KING_KEY){
        myKingRow = getPieceRow(p);
        myKingCol = getPieceCol(p);
        }
    }
    for(PieceInterface teamNumPiece : getTeams().get(otherTeamNum).getActive()){
       if(compareValidMoves(teamNumPiece, myKingRow, myKingCol)){
        return true;
      }
    }

    return false;
  }


  /**
   * Method that checks to see if a given piece contains moves that have a king's coordinates, meaning
   * that they have a move that puts the king in check
   * @param piece the piece whose moves we are analyzing to see if they contain the king
   * @param kingRow the king's x coordinate that we are checking
   * @param kingCol the king's y coordinate that we are checking
   * @return whether the piece in question has possible moves that contain the king's x and y
   */
  protected boolean compareValidMoves(PieceInterface piece, int kingRow, int kingCol){
    int r = getPieceRow(piece);
    int c = getPieceCol(piece);
    for(Move m : getAllValidMovesFor(r, c)){
      if(m.deltaRow() == kingRow && m.deltaCol() == kingCol)
        return true;
    }
    return false;
  }

  /**
   * Method that checks to see if castling is possible with the king and the right rook
   * @return whether the current active team can castle with the king and the right rook
   */
  public boolean canCastleRightRook() {
    boolean firstRook = true;
    for (PieceInterface piece : getActiveTeam().getActive()) {
      if (piece.getKey() == KING_KEY) { //if king is in check, or has moved, return false
        if (((KingPiece) piece).isInCheck() || ((KingPiece) piece).getHasMoved()) {
          return false;
        }
      } else if (piece.getKey() == ROOK_KEY) { //if rook has moved, return false
        if (firstRook) {
          firstRook = false; //skip the first rook, get to the next rook on the board
        } else if (((DynamicPiece) piece).getHasMoved()) {
          return false;
        }
      }
    }
    return true;
  }


  /**
   * Method that checks to see if castling is possible with the king and the left rook
   *
   * @return whether the current active team can castle with the king and the left rook
   */
  public boolean canCastleLeftRook() {
    boolean firstRook = true;
    for (PieceInterface piece : getActiveTeam().getActive()) {
      if (piece.getKey() == KING_KEY) { //if king is in check, or has moved, return false
        if (((KingPiece) piece).isInCheck() || ((KingPiece) piece).getHasMoved()) {
          return false;
        }
      } else if (piece.getKey() == ROOK_KEY) { //if rook has moved, return false
        if (firstRook && ((DynamicPiece) piece).getHasMoved()) {
          return false;
        }
      }
    }
    return true;
  }

}
