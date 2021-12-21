package ooga.model.piece;

import java.util.ArrayList;
import java.util.List;
import ooga.model.util.Direction;
import ooga.model.util.Layout;
import ooga.model.util.Move;
import ooga.model.util.Stream;

public abstract class Piece {

  private static final int NUMBER_OF_DIRECTIONS = 4;
  private static final String SEMICOLON = ";";
  private static final Move INVERT_ROW = new Move(-1, 1);
  private static final Move INVERT_COL = new Move(1, -1);
  private int team;
  private char key;
  private int points;
  private List<Move> possibleMoves;
  private int moveIterations;


  /**
   * default constructor used to create default Piece for testing
   */
  public Piece() {
    this.key = ' ';
    this.team = -1;
    possibleMoves = new ArrayList<>();
    this.points = 0;
    moveIterations = -1;
  }
  /**
   * @Deprecated
   * we Pieces are made from strings
   */
  /*public Piece(char identifier, int teamNum){
    this.key = identifier;
    this.team = teamNum;
    possibleMoves = new ArrayList<>();
    //setPieceProperties();
  }*/


  /**
   * creates Piece and initializes all appropriate values
   *
   * @param identifier key into the properties file for the piece type
   * @param teamNum    number corresponding to team
   * @param data       properly initialized Stream with data to initialize Piece
   */
  public Piece(char identifier, int teamNum, Stream data) {
    this.key = identifier;
    this.team = teamNum;
    possibleMoves = new ArrayList<>();
    setPieceProperties(data);
  }

  /**
   * @Deprecated
   * unused because piece properties are set using a Stream now
   */
  //protected abstract void setPieceProperties();

  /**
   * sets all properties of a piece properly different implemenation for every concrete class
   *
   * @param data properly initialized Stream with data to initialize Piece
   */
  protected abstract void setPieceProperties(Stream data);

  /**
   * returns true if this move is valid given currentLayout, false otherwise
   *
   * @param r             row value of possible Move
   * @param c             col value of possible Move
   * @param currentLayout holds current row and col of Piece to be moved and a TeamStatus for every
   *                      location on board
   * @return
   */
  protected abstract boolean isValidMove(int r, int c, Layout currentLayout);

  /**
   * @return key value for this piece
   */
  public char getKey() {
    return key;
  }

  /**
   * @return point value for this piece
   */
  public int getPoints() {
    return points;
  }

  /**
   * @param points new point value for this piece
   */
  public void setPoints(int points) {
    this.points = points;
  }

  /**
   * @return a list of all possible moves for this piece
   */
  public List<Move> getPossibleMoves() {
    return possibleMoves;
  }

  /**
   * @param moves new list of moves for this piece
   */
  public void setPossibleMoves(List<Move> moves) {
    possibleMoves = moves;
  }

  /**
   * @return this piece's team number
   */
  public int getTeam() {
    return team;
  }

  /**
   * @param team new team number for this piece
   */
  public void setTeam(int team) {
    this.team = team;
  }

  /**
   * takes moves as string from JSON and converts to List<Move>
   *
   * @param moves is a string containing all the possible moves for a piece
   * @return a list of moves
   */
  protected List<Move> parseMoves(String moves) {
    String[] movesArray = moves.split(SEMICOLON);
    List<Move> movesList = new ArrayList<>();
    for (String s : movesArray) {
      movesList.add(new Move(s));
    }
    return movesList;
  }

  /**
   * @Deprecated
   * unused because this class has been refactored to take in a Stream object which holds the data
   */
  /*protected JSONObject setupStream(String path) {
    JSONObject jsonObject = null;
    try (InputStream input = Piece.class.getClassLoader().getResourceAsStream(path)) {
      Properties prop = new Properties();
      if (input == null) {
        System.out.println("Invalid file " + path);
        return null;
      }
      prop.load(input);

      String configuration = prop.getProperty(Character.toString(key));
      jsonObject = new JSONObject(configuration);
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    return jsonObject;
  }*/

  /**
   * alters all moves passed in to be oriented in a specified direction
   *
   * @param moves     list of moves to be oriented
   * @param direction moves should be oriented in this direction
   * @return a properly oriented List<Move>
   */
  protected List<Move> orientMoves(List<Move> moves, Direction direction) {
    List<Move> orientedMoves = new ArrayList<>();
    for (Move m : moves) {
      orientedMoves.add(orient(m, direction));
    }
    return orientedMoves;
  }

  /**
   * alters a move to be oriented in a specified direction
   *
   * @param m         a single move to be oriented
   * @param direction move should be oriented in this direction
   * @return a properly oriented Move
   */
  protected Move orient(Move m, Direction direction) {
    switch (direction) {
      case DOWN:
        return m.multiply(INVERT_ROW);
      case RIGHT:
        return m.swapRowCol().multiply(INVERT_COL);
      case LEFT:
        return m.swapRowCol();
      default:
        return m;
    }
  }

  /**
   * @return the proper direction based on this piece's team number
   */
  public Direction findDirection() {
    int directionNum = getTeam() % NUMBER_OF_DIRECTIONS;
    switch (directionNum) {
      case 1:
        return Direction.UP;
      case 2:
        return Direction.DOWN;
      case 3:
        return Direction.RIGHT;
      case 0:
        return Direction.LEFT;
      default:
        return null;
    }
  }

  /**
   * @return move iteration value for this piece
   */
  public int getMoveIterations() {
    return moveIterations;
  }

  /**
   * @param i new iterations for this piece
   */
  public void setMoveIterations(int i) {
    moveIterations = i;
  }

}
