package ooga.model.board;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.management.ReflectionException;

import ooga.model.piece.*;
import ooga.model.team.Team;
import ooga.model.team.TeamInterface;
import ooga.model.util.Grid;
import ooga.model.util.Layout;
import ooga.model.util.Move;
import ooga.model.util.Stream;
import org.json.JSONObject;

public abstract class Board implements BoardInterface {
  public static final String KING = "King";
  public static final String QUEEN = "Queen";
  public static final String ROOK = "Rook";
  public static final String BISHOP = "Bishop";
  public static final String PAWN = "Pawn";
  public static final String KNIGHT = "Knight";
  public static final char KING_KEY = 'K';
  public static final char QUEEN_KEY = 'Q';
  public static final char ROOK_KEY = 'R';
  public static final char BISHOP_KEY = 'B';
  public static final char PAWN_KEY = 'P';
  public static final char KNIGHT_KEY = 'N';

  private static final String PIECE_PROPERTY_PATH = "ooga/model/resources/chess.properties";
  protected static final Integer SINGLE_ITER = 1;
  protected static final int ZERO = 0;
  public static final char CHECKERS_NORMAL = 'C';
  private static final String CLASS_PATH = "ooga.model.piece.";
  private static final String PIECE_TYPE = "Type";
  private static final int ZERO_INDEX = 0;
  private static final int FIRST_INDEX = 1;
  private static final String CHESS = "chess";
  private static final CharSequence BIGBOARD_CHESS = "BigBoardChess";
  private static final String EMPTY = "";
  private List<List<PieceInterface>> boardPieces;
  private PieceInterface currentPiece;
  private TeamInterface activeTeam;
  private Map<Integer, TeamInterface> teams;
  private List<Move> validMoves;
  private int currentRow;
  private int currentCol;
  private int teamWon;
  private List<Integer> teamNumbers;
  private PieceInterface pawnPromotionPiece;

  /**
   * create a new default board
   */
  public Board() {
    boardPieces = new ArrayList<>();
    validMoves = new ArrayList<>();
    teams = new HashMap<>();
    teamWon = -1;
    teamNumbers = new ArrayList<>();
  }

  /**
   * add a team with a specified number to the teams in this game
   *
   * @param num is the number associated with the team to be added
   */
  private void initializeTeam(int num) {
    teams.putIfAbsent(num, new Team(num));
  }

  /**
   * Given a Grid object consisting of a board initial configuration csv file, initialize the
   * boardPieces with the proper pieces and their properties as well as the current active team
   * playing the game
   *
   * @param parsedBoard, the parsed board read from a data/boards csv file
   */
  @Override
  public void initialize(Grid parsedBoard, String piecePath)
      throws ReflectionException {
    for (int i = 0; i < parsedBoard.length(); i++) {
      List<PieceInterface> rowPieces = new ArrayList<>();
      for (int j = 0; j < parsedBoard.width(); j++) {
        if (parsedBoard.getVal(i, j).equals(EMPTY)) {
          rowPieces.add(null);
        } else {
          String nextPiece = parsedBoard.getVal(i, j);
          String propertyPath = (piecePath.contains(BIGBOARD_CHESS)) ? CHESS : piecePath;
          PieceInterface tempPiece = makePiece(nextPiece, propertyPath);
          addPieceToTeam(tempPiece);
          rowPieces.add(tempPiece);
        }
      }
      boardPieces.add(rowPieces);
    }
    teamNumbers.addAll(teams.keySet());
    activeTeam = teams.get(Integer.valueOf(Collections.min(teams.keySet())));
  }

  private void addPieceToTeam(PieceInterface piece) {
    initializeTeam(piece.getTeam());
    teams.get(piece.getTeam()).setActivePiece(piece);
  }

//  @Deprecated
//  private PieceInterface initializeNewTeam(int i, int j, Grid parsedBoard) {
//    char pieceKey = parsedBoard.getVal(i, j).charAt(ZERO_INDEX); // getPiecekey
//    int teamNum =
//        Integer.valueOf(String.valueOf(parsedBoard.getVal(i, j).charAt(FIRST_INDEX))); // getTeamNum
//    PieceInterface tempPiece = new ChessPiece(pieceKey, teamNum); // needs to use reflection
//    initializeTeam(teamNum); // initialize team with teamnum
//    TeamInterface tempTeam = teams.get(teamNum); // retrieve team
//    tempTeam.setActivePiece(tempPiece); // addnewpiece to active
//    return tempPiece;
//  }

  protected PieceInterface makePiece(String nextPiece, String path)
      throws ReflectionException {
    char pieceKey = nextPiece.charAt(ZERO_INDEX);
    int teamNum = Integer.valueOf(String.valueOf(nextPiece.charAt(FIRST_INDEX)));
    String type = new String();
    Stream configuration = null;

    return makePieceFromStream(teamNum, pieceKey, type, configuration, path);
  }

  private PieceInterface makePieceFromStream(int teamNum, char pieceKey, String type,
      Stream configuration, String path)
      throws ReflectionException {
    try (InputStream input = ChessBoard.class.getClassLoader().getResourceAsStream(path)) {
      Properties prop = new Properties();
      if (input == null) {
        return null;
      }
      prop.load(input);
      String key = prop.getProperty(Character.toString(pieceKey));
      JSONObject jsonObject = new JSONObject(key);
      configuration = new Stream(pieceKey, jsonObject);
      type = configuration.getData().get(PIECE_TYPE).toString();

    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return initializeReflectedPiece(pieceKey, teamNum, configuration, type);
  }

  private PieceInterface initializeReflectedPiece(
      char pieceKey, int teamNum, Stream configuration, String type) throws ReflectionException {
    try {
      Class<?> piece = Class.forName(CLASS_PATH + type);
      Constructor constructor = piece.getDeclaredConstructor(char.class, int.class, Stream.class);
      PieceInterface p = (PieceInterface) constructor.newInstance(pieceKey, teamNum, configuration);
      return p;
    } catch (RuntimeException
        | ClassNotFoundException
        | NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  }

  /*
  abstract method that finds valid moves
  */
  public abstract void findValidMoves(int row, int col);

  /**
   * Given the correct edge of the board, check to see if any of the current active team's pawns has
   * reached the other end
   *
   * @return whether a pawn has reached the other end and pawning can happen
   */
  public boolean checkPawnPromotion() {
    List<PieceInterface> edgePieces = getPawnEdgeList();
    for (PieceInterface piece : edgePieces) {
      if (piece != null && properPromotionKey(piece) && piece.getTeam() == activeTeam.getTeamNum()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method that determines if the type of piece in question can be promoted. For chess, this is a pawn
   * and for checkers this is a normal piece (can't be a checkers king)
   * @param piece the piece in consideration for promotion
   * @return whether the piece is the kind of piece that can be promoted
   */
  protected boolean properPromotionKey(PieceInterface piece){
    return piece.getKey() == PAWN_KEY || piece.getKey() == CHECKERS_NORMAL;
  }

  /**
   * Method that produces a list of the pieces at the edge which we want to check for depending on
   * the team number (team 1 is the top, team 2 is the bottom, team 3 is right, team 4 is left
   * edge)
   *
   * @return a List of PieceInterface objects representing the correct edge of the board to help in
   * checking for pawning
   */
  private List<PieceInterface> getPawnEdgeList() {
    switch (activeTeam.getTeamNum()) {
      case 1:
        return boardPieces.get(0);
      case 2:
        return boardPieces.get(boardPieces.size() - 1);
      case 3:
        return rightEdge();
      case 4:
        return leftEdge();
      default:
        return null;
    }
  }

  /**
   * Calculates the left edge of the board
   *
   * @return a List of the PieceInterfaces making up the left edge of the board
   */
  private List<PieceInterface> leftEdge() {
    List<PieceInterface> leftEdge = new ArrayList<>();
    for (int row = 0; row < boardPieces.size(); row++) {
      leftEdge.add(boardPieces.get(row).get(0));
    }
    return leftEdge;
  }

  /**
   * Calculates the right edge of the board
   *
   * @return a List of the PieceInterfaces making up the right edge of the board
   */
  private List<PieceInterface> rightEdge() {
    List<PieceInterface> rightEdge = new ArrayList<>();
    for (int row = 0; row < boardPieces.size(); row++) {
      rightEdge.add(boardPieces.get(row).get(boardPieces.size() - 1));
    }
    return rightEdge;
  }

  /**
   * Given that pawning can happen, this method is called to execute the update
   *
   * @param newPieceId the new type of piece replacing the pawn
   */
  public void promotePawn(char newPieceId) {
    for (PieceInterface piece : getPawnEdgeList()) {
      if (piece != null && properPromotionKey(piece) && piece.getTeam() == activeTeam.getTeamNum()) {
        StringBuilder pieceString = new StringBuilder();
        pieceString.append(newPieceId).append(activeTeam.getTeamNum());
        pawnPromotionPiece = null;
        try {
          pawnPromotionPiece = makePiece(pieceString.toString(), PIECE_PROPERTY_PATH);
        } catch (ReflectionException e) {
          e.printStackTrace();
        }
        boardPieces.get(getPieceRow(piece)).set(getPieceCol(piece), pawnPromotionPiece);
      }
    }
  }

  /**
<<<<<<< HEAD
   * Method that checks to see if castling is possible with the king and the right rook
   * @return whether the current active team can castle with the king and the right rook
   */
  public boolean canCastleRightRook() {
    //king has not moved AND is not in check
    //rook has not moved
    //moving king would not put king in check
    //no pieces bewteen king and rook
    boolean firstRook = true;
    for (PieceInterface piece : activeTeam.getActive()) {
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
    return false;
  }


  /**
   * Method that checks to see if castling is possible with the king and the left rook
   *
   * @return whether the current active team can castle with the king and the left rook
   */
  public boolean canCastleLeftRook() {
    boolean firstRook = true;
    for (PieceInterface piece : activeTeam.getActive()) {
      if (piece.getKey() == 'K') { //if king is in check, or has moved, return false
        if (((KingPiece) piece).isInCheck() || ((KingPiece) piece).getHasMoved()) {
          return false;
        }
      } else if (piece.getKey() == 'R') { //if rook has moved, return false
        if (firstRook && ((DynamicPiece) piece).getHasMoved()) {
          return false;
        }
      }
    }
    return false;
  }

  /**
=======
>>>>>>> master
   * Method that retrieves the row given a piece object
   *
   * @param piece the piece for which we are finding the row
   * @return row of the piece
   */
  public int getPieceRow(PieceInterface piece) {
    for (int row = 0; row < boardPieces.size(); row++) {
      if (boardPieces.get(row).contains(piece)) {
        return row;
      }
    }
    return -1;
  }

  /**
   * Method that retrieves the column given a piece object
   *
   * @param piece the piece for which we are finding the column
   * @return column of the piece
   */
  public int getPieceCol(PieceInterface piece) {
    for (int row = 0; row < boardPieces.size(); row++) {
      for (int col = 0; col < boardPieces.get(0).size(); col++) {
        if (boardPieces.get(row).get(col) != null && boardPieces.get(row).get(col).equals(piece)) {
          return col;
        }
      }
    }
    return -1;
  }

  protected PieceInterface makePieceShortcut(char key, int teamNum){
    StringBuilder pieceString = new StringBuilder();
    pieceString.append(key).append(teamNum);
    PieceInterface piece = null;
    try {
      piece = makePiece(pieceString.toString(), PIECE_PROPERTY_PATH);
    } catch (ReflectionException e) {
      e.printStackTrace();
    }
    return piece;
  }

  /**
   * Method that executes the switching of right rook and the king
   */
  @Deprecated
  public void castleRightRook() {
//    boolean firstRook = true;
//    for (PieceInterface piece : getActiveTeam().getActive()) {
//      if (piece.getKey() == KING_KEY && piece.getTeam() == getActiveTeam().getTeamNum()) {
//        PieceInterface rook = makePieceShortcut(ROOK_KEY, getActiveTeam().getTeamNum());
//        getBoardPieces().get(getPieceRow(piece)).set(getPieceCol(piece), rook);
//      } else if (piece.getKey() == ROOK_KEY && piece.getTeam() == getActiveTeam().getTeamNum()) {
//        if (firstRook) {
//          firstRook = false;
//        } else{
//          PieceInterface king = makePieceShortcut(KING_KEY, getActiveTeam().getTeamNum());
//          getBoardPieces().get(getPieceRow(piece)).set(getPieceCol(piece), king);
//        }
//      }
//    }
  }

  /**
   * Method that executes the switching of left rook and the king
   */
  @Deprecated
  public void castleLeftRook() {
//    boolean firstRook = true;
//    for (PieceInterface piece : getActiveTeam().getActive()) {
//      if (piece.getKey() == KING_KEY && piece.getTeam() == getActiveTeam().getTeamNum()) {
//        PieceInterface rook = makePieceShortcut(ROOK_KEY, getActiveTeam().getTeamNum());
//        getBoardPieces().get(getPieceRow(piece)).set(getPieceCol(piece), rook);
//      } else if (piece.getKey() == ROOK_KEY && piece.getTeam() == getActiveTeam().getTeamNum()) {
//        if (firstRook) {
//          PieceInterface king = makePieceShortcut(KING_KEY, getActiveTeam().getTeamNum());
//          getBoardPieces().get(getPieceRow(piece)).set(getPieceCol(piece), king);
//          firstRook = false;
//        }
//      }
//    }
  }

  /*
 abstract method that finds which team has won if any
 */
  protected abstract void hasWon();

  /**
   * Clears all variables that keep track of the current game
   */
  @Override
  public void clearGame() {
    boardPieces.clear();
    validMoves.clear();
    teams.clear();
    activeTeam = null;
    currentPiece = null;
  }

  /**
   * sets the active team appropriately for the next turn
   */
  public void setActiveTeam() {
    int currTeam = activeTeam.getTeamNum();
    currTeam++;
    if (teamNumbers.contains(currTeam)) {
      activeTeam = teams.get(currTeam);
    } else {
      activeTeam = teams.get(teamNumbers.get(ZERO_INDEX));
    }
  }

  /**
   * Method that loops through all other teams and checks for any pieces that the current
   * active team has gotten captured
   * @return a list of the current active team's captured pieces by other teams
   */
  public ArrayList<String> getCapturedPieces() {
    ArrayList<String> pieceNames = new ArrayList<>();
    for(int teamNum : teamNumbers){
      if(teamNum != activeTeam.getTeamNum()){
        List<PieceInterface> captured = teams.get(teamNum).getCaptured().get(activeTeam.getTeamNum());
        addCapturedString(captured, pieceNames);
      }
    }
    return pieceNames;
  }

  protected void addCapturedString(List<PieceInterface> captured, ArrayList<String> pieceNames){
    for(PieceInterface piece : captured){
      if(!pieceNames.contains(piece.getKey())){
        pieceNames.add(getPieceString(piece.getKey()));
      }
    }
  }

  protected String getPieceString(char key){
    switch(key){
      case KNIGHT_KEY: return KNIGHT;
      case KING_KEY: return KING;
      case QUEEN_KEY: return QUEEN;
      case ROOK_KEY: return ROOK;
      case BISHOP_KEY: return BISHOP;
      case PAWN_KEY: return PAWN;
      default: return null;
    }
  }

  /**
   * Method that returns the team number that has won the game, assuming the game has been won
   *
   * @return the global variable that keeps track of the team that won the game
   */
  public int getHasWon() {
    return teamWon;
  }

  /**
   * Method that returns a list of possible moves of the piece a user has clicked on, on the proper
   * team
   *
   * @return a list of valid moves of the piece
   */
  public List<Move> getValidMoves() {
    return validMoves;
  }

  /**
   * Method that returns the list of the numbers of teams
   * @return the integer numbers of the teams
   */
  public List<Integer> getTeamNumbers(){
    return teamNumbers;
  }

  /**
   * returns the piece
   *
   * @param r the row value of the piece we want to retrieve
   * @param c the col value of the piece we want to retrieve
   * @return
   */
  public PieceInterface getPieceAt(int r, int c) {

    return boardPieces.get(r).get(c);
  }

  /**
   * method that returns the team object of the active team
   *
   * @return team interface object of the active team
   */
  public TeamInterface getActiveTeam() {
    return activeTeam;
  }

  /**
   * updates the piece to move from the old position to the new position on the board
   *
   * @param r the new row position of the piece
   * @param c the new col position of the piece
   */
  protected void updatePieces(int r, int c) {
    boardPieces.get(r).set(c, currentPiece);
    boardPieces.get(currentRow).set(currentCol, null);
  }

  /**
   * gets the current (original) row of the piece
   *
   * @return integer of the row
   */
  public int getCurrentRow() {
    return currentRow;
  }

  protected void setCurrentRow(int r) {
    currentRow = r;
  }

  /**
   * get the current column of piece chosen
   *
   * @return int of the current column
   */
  public int getCurrentCol() {
    return currentCol;
  }

  protected void setCurrentCol(int col) {
    currentCol = col;
  }

  protected PieceInterface getCurrentPiece() {
    return currentPiece;
  }

  protected void setCurrentPiece(PieceInterface p) {
    currentPiece = p;
  }


  protected Map<Integer, TeamInterface> getTeams() {
    return teams;
  }

  protected void setTeamWon(int i) {
    teamWon = i;
  }

  protected void updateTeamVals(int r, int c) {
    if (getPieceAt(r, c) != null) {
      PieceInterface updatePiece = getPieceAt(r, c);
      getTeam(updatePiece.getTeam()).removeActive(updatePiece);
      activeTeam.setCaptured(updatePiece.getTeam(), updatePiece);
      boardPieces.get(r).set(c, null);
    }
  }

  protected void pieceJumped(int r, int c) {
    boardPieces.get(r).set(c, null);
  }

  /**
   * Removes selected piece from the board permanently and irrespective of team
   *
   * @param r the new row position of the piece
   * @param c the new col position of the piece
   */
  protected void removePiece(int r, int c) {
    boardPieces.get(r).set(c, null);
  }

  /**
   * creates piece at given index according to the current piece. Not assigned to a team
   *
   * @param r the new row position of the piece
   * @param c the new col position of the piece
   */
  protected void createPiece(int r, int c) {
    boardPieces.get(r).set(c, currentPiece);
  }

  /**
   * determines if a Move is legal
   *
   * @param row the row position of the new move
   * @param col the column positino of the new move
   * @return true if the Move is legal, false otherwise
   */
  protected boolean validateGeneralMove(int row, int col) {
    // changed boardPieces.size to getBoardLength() --> might need to debug
    if (!validBounds(row, col)) {
      return false;
    }
    if (validateNewPosition(row, col)) {
      return false;
    }
    return true;
  }

  protected abstract boolean validateNewPosition(int row, int col);


  protected void addValidMove(Move m) {
    validMoves.add(m);
  }

  protected int getBoardLength() {
    return boardPieces.size();
  }

  protected void clearPossibleMoves() {
    validMoves.clear();
  }

  protected TeamInterface getTeam(int num) {
    if (teams.containsKey(num)) {
      return teams.get(num);
    } else {
      return null;
    }
  }

  protected int getBoardWidth() {
    return boardPieces.get(ZERO_INDEX).size();
  }


  protected boolean validBounds(int row, int col) {
    return row >= 0 && row < getBoardLength() && col >= 0 && col < getBoardWidth();
  }

  protected List<List<PieceInterface>> getBoardPieces() {
    return boardPieces;
  }

  public PieceInterface getPawnPromotionPiece(){return pawnPromotionPiece;}

}
