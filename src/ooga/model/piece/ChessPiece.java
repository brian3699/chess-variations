package ooga.model.piece;

import ooga.model.util.Layout;
import ooga.model.util.Stream;
import ooga.model.util.TeamStatus;
import org.json.*;

/*resources:
  https://www.javatpoint.com/how-to-get-value-from-json-object-in-java-example
  https://mkyong.com/java/java-properties-file-examples/
 */


public class ChessPiece extends Piece implements PieceInterface {

  private static final String POINTS = "Points";
  private static final String ITERATIONS = "Iterations";
  private static final String MOVES = "Moves";


  /**
   * creates ChessPiece and initializes all appropriate values
   *
   * @param identifier    key into the properties file for the piece type
   * @param teamNum       number corresponding to team
   * @param configuration properly initialized Stream with data to initialize Piece
   */
  public ChessPiece(char identifier, int teamNum, Stream configuration) {
    super(identifier, teamNum, configuration);
  }

  /**
   * @Deprecated
   * we now always pass in a Stream to make pieces
   */
  /*public ChessPiece(char identifier, int teamNum){
    super(identifier, teamNum);
  }*/

  /**
   * creates default ChessPiece used for easy testing
   */
  public ChessPiece() {
    super();
  }

  /**
   * initializes points, moveIterations, and possibleMoves from input properties file
   */
  @Override
  public void setPieceProperties(Stream configuration) {
    JSONObject jsonObject = configuration.getData();
    setPoints(Integer.parseInt(jsonObject.get(POINTS).toString()));
    setMoveIterations(Integer.parseInt(jsonObject.get(ITERATIONS).toString()));
    setPossibleMoves(parseMoves(jsonObject.get(MOVES).toString()));
  }

  @Override
  /**
   * determines if a move, specified by moveRow and moveCol, is valid based on currentLayout
   * @return true if move is valid, false otherwise
   */
  public boolean isValidMove(int moveRow, int moveCol, Layout currentLayout) {
    TeamStatus status = currentLayout.get(moveRow, moveCol);
    return status == TeamStatus.EMPTY || status == TeamStatus.OTHER;
  }

}

