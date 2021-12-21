package ooga.view.piece;

import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Creates and returns the node containing the image of the piece.
 *
 * @author Young Jun, Evelyn Cupil-Garcia
 */
public class PieceView extends Region {

  private static final String DEFAULT_PIECE_BACKGROUND_RESOURCES_PATH = "ooga.view.resources.pieceDirectory.pieceBackground";
  private static final String DEFAULT_VALUES_RESOURCES_PATH = "ooga.view.resources.pieceDirectory.magicValues";
  private static final String CHRISTMAS_CHESS = "ChristmasChess";
  private static final String GRID_SIZE = "GridSize";
  private static final String TILE_COLOR_1 = "TileColor1";
  private static final String TILE_COLOR_2 = "TileColor2";

  private final Image pieceImage;
  private HBox pieceNode;
  private ResourceBundle resourceBundle;
  private ResourceBundle myValues;


  /**
   * Constructor for PieceView that draws the piece.
   *
   * @param pieceImage image passed to be displayed.
   */
  public PieceView(Image pieceImage, String gameType) {
    this.pieceImage = pieceImage;
    myValues = ResourceBundle.getBundle(DEFAULT_VALUES_RESOURCES_PATH);
    if (gameType.equals(myValues.getString(CHRISTMAS_CHESS))) {
      resourceBundle = ResourceBundle.getBundle(DEFAULT_PIECE_BACKGROUND_RESOURCES_PATH + gameType);
    } else {
      resourceBundle = ResourceBundle.getBundle(DEFAULT_PIECE_BACKGROUND_RESOURCES_PATH);
    }
    drawPiece();
  }

  // Initialize pieceNode and inserts an image.
  private void drawPiece() {
    pieceNode = new HBox();
    Rectangle myPiece = new Rectangle(Integer.parseInt(myValues.getString(GRID_SIZE)),
        Integer.parseInt(myValues.getString(GRID_SIZE)));
    myPiece.setFill(new ImagePattern(pieceImage));
    pieceNode.getChildren().add(myPiece);
  }

  /**
   * Method that set's the background of the piece.
   *
   * @param rowNum row number of the location of the piece.
   * @param colNum column number of the location of the piece.
   */
  public void setPieceBackground(int rowNum, int colNum) {
    if ((rowNum + colNum) % 2 == 0) {
      pieceNode.setStyle(resourceBundle.getString(myValues.getString(TILE_COLOR_1)));
    } else {
      pieceNode.setStyle(resourceBundle.getString(myValues.getString(TILE_COLOR_2)));
    }
  }

  /**
   * get resourceBundle of piece
   *
   * @return ResourceBundle
   */
  public ResourceBundle getResourceBundle() {
    return resourceBundle;
  }

  /**
   * Getter method of pieceNode.
   *
   * @return pieceNode.
   */
  public Node getPiece() {
    return pieceNode;
  }
}
