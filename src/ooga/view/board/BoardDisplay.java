package ooga.view.board;

import java.awt.Point;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import ooga.model.util.Move;
import ooga.view.piece.PieceView;

/**
 * Implements the BoardDisplayInterface
 *
 * @author Evelyn Cupil-Garcia, Young Jun
 */
public class BoardDisplay implements BoardDisplayInterface {

  private static final String DEFAULT_PIECE_RESOURCES_PATH = "ooga.view.resources.pieceDirectory.PieceColor";
  private static final String DEFAULT_VALUES_RESOURCES_PATH = "ooga.view.resources.pieceDirectory.magicValues";
  private static final String TILE_COLOR_1 = "TileColor1";
  private static final String TILE_COLOR_2 = "TileColor2";
  private static final String TILE_GREEN = "TileGreen";
  private static final String BOARD_ID = "Board";
  private static final int BOARD_SIZE = 600;
  private GridPane boardGrid;
  private Node[][] myChessPieces;
  private final Map<String, Image> pieceMap;
  private Consumer clickConsumer;
  private String gameType;
  private ResourceBundle resourceBundle;
  private ResourceBundle myValues;


  /**
   * Constructor that initializes BoardView.
   *
   * @param clickConsumer controller method for click cell
   */
  public BoardDisplay(Consumer<Point> clickConsumer, String gameType) {
    this.clickConsumer = clickConsumer;
    boardGrid = new GridPane();
    pieceMap = new HashMap<>();
    this.gameType = gameType;
    populatePieceMap(pieceMap);
    myValues = ResourceBundle.getBundle(DEFAULT_VALUES_RESOURCES_PATH);
  }

  /**
   * Updates a piece on the board.
   *
   * @param x    row number of piece.
   * @param y    column number of piece.
   * @param tag  piece type.
   * @param team piece's team.
   */
  @Override
  public void update(int x, int y, char tag, int team) {
    String pieceType = Character.toString(tag) + team;
    PieceView piece = new PieceView(pieceMap.get(pieceType), gameType);
    piece.setPieceBackground(x, y);
    setPieceClickAction(piece.getPiece(), x, y);
    boardGrid.getChildren().remove(getChessPiece(x, y));
    myChessPieces[x][y] = piece.getPiece();
    boardGrid.add(myChessPieces[x][y], y, x);
  }

  /**
   * set color of the board
   *
   * @param colorHex color
   */
  public void setColor(String colorHex) {
    // TODO: Not sure how to override resource bundle given current design structure
    System.out.println(colorHex);
  }

  private void populatePieceMap(Map<String, Image> map) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(DEFAULT_PIECE_RESOURCES_PATH);
    Enumeration<String> enumeration = resourceBundle.getKeys();
    while (enumeration.hasMoreElements()) {
      String s = enumeration.nextElement();
      map.put(s, new Image(resourceBundle.getString(String.valueOf(s))));
    }
  }

  /**
   * Getter method of boardGrid.
   *
   * @return boardGrid in Node
   */
  public GridPane getBoardGrid() {
    boardGrid.setPrefWidth(BOARD_SIZE);
    boardGrid.setPrefHeight(BOARD_SIZE);
    return boardGrid;
  }

  /**
   * Draw's a piece on the board.
   *
   * @param tag    piece type.
   * @param rowNum row column of piece.
   * @param colNum column number of piece.
   * @param team   team number of piece.
   */
  public void drawBoard(char tag, int rowNum, int colNum, int team) {
    String pieceType = Character.toString(tag) + team;
    PieceView piece = new PieceView(pieceMap.get(pieceType), gameType);
    resourceBundle = piece.getResourceBundle();
    setPieceClickAction(piece.getPiece(), rowNum, colNum);
    piece.setPieceBackground(rowNum, colNum);
    myChessPieces[rowNum][colNum] = piece.getPiece();
    boardGrid.add(myChessPieces[rowNum][colNum], colNum, rowNum);
  }

  /**
   * set grid lines
   */
  public void setGridLines() {
    boardGrid.setGridLinesVisible(true);
    boardGrid.setId(BOARD_ID);
  }

  private HBox getChessPiece(int rowNum, int colNum) {
    return (HBox) myChessPieces[rowNum][colNum];
  }

  private void setPieceClickAction(Node myPiece, int rowNum, int colNum) {
    myPiece.setOnMouseClicked(event -> clickConsumer.accept(new Point(rowNum, colNum)));

  }

  /**
   * Highlight possible moves in green.
   *
   * @param possibleMoves List of points with possible moves.
   */
  public void showPossibleMoves(List<Move> possibleMoves) {
    for (Move move : possibleMoves) {
      HBox piece = getChessPiece(move.deltaRow(), move.deltaCol());
      piece.setStyle(resourceBundle.getString(myValues.getString(TILE_GREEN)));
      System.out.println(piece.getStyle() + move);
    }
  }

  /**
   * Removes possible moves that are in green.
   *
   * @param possibleMoves List of points with possible moves.
   */
  public void removePossibleMoves(List<Move> possibleMoves) {
    for (Move move : possibleMoves) {
      HBox piece = getChessPiece(move.deltaRow(), move.deltaCol());
      if ((move.deltaRow() + move.deltaCol()) % 2 == 0) {
        piece.setStyle(resourceBundle.getString(myValues.getString(TILE_COLOR_1)));
      } else {
        piece.setStyle(resourceBundle.getString(myValues.getString(TILE_COLOR_2)));
      }
    }
  }

  /**
   * Initializes board with grid lines.
   */
  public void setBoard(int row, int col) {
    myChessPieces = new Node[row][col];
  }
}
