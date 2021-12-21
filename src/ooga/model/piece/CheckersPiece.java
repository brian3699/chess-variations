package ooga.model.piece;

import java.util.List;
import ooga.model.util.Direction;
import ooga.model.util.Layout;
import ooga.model.util.Move;
import ooga.model.util.Stream;
import org.json.JSONObject;

public class CheckersPiece extends Piece implements PieceInterface {

  private static final String POINTS = "Points";
  private static final String MOVES = "Moves";
  private static final String CAPTURE_MOVES = "CaptureMoves";
  private Direction direction;
  private final String CHECKERS_PROPERTY_PATH = "ooga/model/resources/checkers.properties";
  private List<Move> captureMoves;

  /**
   * creates default Checkers piece used for easy testing
   */
  public CheckersPiece() {
    super();
  }

  /**
   * creates CheckersPiece and initializes all appropriate values
   *
   * @param identifier    key into the properties file for the piece type
   * @param teamNum       number corresponding to team
   * @param configuration properly initialized Stream with data to initialize Piece
   */
  public CheckersPiece(char identifier, int teamNum, Stream configuration) {
    super(identifier, teamNum, configuration);
  }

  @Override
  /**
   * sets all properties of a CheckersPiece based on data stored in configuration
   * including points, possibleMoves, captureMoves, and sets direction
   */
  public void setPieceProperties(Stream configuration) {
    JSONObject jsonObject = configuration.getData();
    direction = findDirection();
    setPoints(Integer.parseInt(jsonObject.get(POINTS).toString()));
    setPossibleMoves(orientMoves(parseMoves(jsonObject.get(MOVES).toString()), direction));
    captureMoves = orientMoves(parseMoves(jsonObject.get(CAPTURE_MOVES).toString()), direction);
  }

  @Override
  /**
   * currently not implemented due to time constraints
   * should return true if a move is valid based on moveRow/moveCol & currentLayout, false otherwise
   */
  public boolean isValidMove(int moveRow, int moveCol, Layout currentLayout) {
    return false;
  }

  /**
   * @return the possible capturing moves for this CheckersPiece
   */
  public List<Move> getCaptureMoves() {
    return captureMoves;
  }
}
