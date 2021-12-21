package ooga.model.piece;

import java.util.ArrayList;
import java.util.List;
import ooga.model.util.Direction;
import ooga.model.util.Layout;
import ooga.model.util.Move;
import ooga.model.util.Stream;
import ooga.model.util.TeamStatus;
import org.json.JSONObject;

public class PawnPiece extends DynamicPiece implements PieceInterface {

  private static final String INITIAL_MOVES = "InitialMoves";
  private static final int ZERO = 0;
  private static final int TWO = 2;
  private Direction direction;
  private List<Move> initialPossibleMoves;

  /**
   * creates Pawn Piece and initializes all appropriate values
   *
   * @param identifier    key into the properties file for the piece type
   * @param teamNum       number corresponding to team
   * @param configuration properly initialized Stream with data to initialize Piece
   */
  public PawnPiece(char identifier, int teamNum, Stream configuration) {
    super(identifier, teamNum, configuration);
    addInitialMoves(configuration);
  }

  /**
   * adds all initial moves specific to pawns
   *
   * @param configuration holds data containing initialMoves
   */
  private void addInitialMoves(Stream configuration) {
    JSONObject jsonObject = configuration.getData();
    initialPossibleMoves = orientMoves(parseMoves(jsonObject.get(INITIAL_MOVES).toString()),
        direction);
    List<Move> alwaysPossibleMoves = orientMoves(super.getPossibleMoves(), direction);
    List<Move> totalMoves = new ArrayList<>();
    totalMoves.addAll(initialPossibleMoves);
    totalMoves.addAll(alwaysPossibleMoves);
    setPossibleMoves(totalMoves);
  }

  @Override
  /**
   * calls superclass's setPieceProperties for all generic properties and sets direction
   */
  public void setPieceProperties(Stream configuration) {
    super.setPieceProperties(configuration);
    direction = findDirection();
  }

  /**
   * @return direction for this PawnPiece
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * @Deprecated
   * unused because Orientation is now a Direction
   */
  /*public Move getOrientation() {
    return null;
  }*/

  /**
   * @Deprecated
   * unused because Orientation is now a Direction
   */
  /*public void setOrientation(Move m) {
    orientation = m;
  }*/

  /**
   * @Deprecated moves are now oriented in orientMoves() and this override is no longer needed
   */
  /*public List<Move> getPossibleMoves(){
    List<Move> orientedMoves = new ArrayList<>();
    System.out.println("PAWN MOVES: " + super.getPossibleMoves());
    for(Move m : super.getPossibleMoves())
      orientedMoves.add(m.multiply(orientation));
    return orientedMoves;
  }*/
  @Override
  /**
   * calls superclass's moved method if necessary, removes initialMoves from possibleMoves
   */
  public void moved() {
    if (!getHasMoved()) {
      super.moved();
    }

    List<Move> totalMoves = super.getPossibleMoves();
    totalMoves.removeAll(initialPossibleMoves);
    setPossibleMoves(totalMoves);
  }

  @Override
  /**
   * determines if move specified by moveRow and moveCol is allowed based on currentLayout,
   * takes into account capturing on diagonals, two space moves, and ordinary single space forward moves
   * @return true if move is allowed, false otherwise
   */
  public boolean isValidMove(int moveRow, int moveCol, Layout currentLayout) {
    int myR = currentLayout.getR();
    int myC = currentLayout.getC();
    TeamStatus target = currentLayout.get(moveRow, moveCol);
    TeamStatus middleSpace = null;
    if (isTwoSpaceMove(myR, myC, moveRow, moveCol)) {
      middleSpace = currentLayout.get((myR + moveRow) / TWO, (myC + moveCol) / TWO);
    }
    if (!isMoveForward(myR, myC, moveRow, moveCol) && target != TeamStatus.OTHER) {
      return false;
    }
    if (isMoveForward(myR, myC, moveRow, moveCol) && target != TeamStatus.EMPTY) {
      return false;
    }
    if (middleSpace == TeamStatus.OTHER || middleSpace == TeamStatus.OWN) {
      return false;
    }

    return true;
  }

  /**
   * determines if this move is a two space move
   *
   * @param myR     this pawn's row
   * @param myC     this pawn's col
   * @param moveRow move resultant row
   * @param moveCol move resultant col
   * @return true if move is two spaces, false otherwise
   */
  private boolean isTwoSpaceMove(int myR, int myC, int moveRow, int moveCol) {
    int dr = Math.abs(myR - moveRow);
    int dc = Math.abs(myC - moveCol);
    return dr == TWO || dc == TWO;
  }

  /**
   * determines if this move is straight forward
   *
   * @param myR     this pawn's row
   * @param myC     this pawn's col
   * @param moveRow move resultant row
   * @param moveCol move resultant col
   * @return true if move is in one direction only, false otherwise
   */
  private boolean isMoveForward(int myR, int myC, int moveRow, int moveCol) {
    int dr = myR - moveRow;
    int dc = myC - moveCol;
    return dr == ZERO || dc == ZERO;
  }

}
