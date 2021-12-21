package ooga.model.piece;

import ooga.model.util.Stream;

public class DynamicPiece extends ChessPiece {

  private boolean hasMoved;

  /**
   * creates KingPiece and initializes all appropriate values
   *
   * @param identifier    key into the properties file for the piece type
   * @param teamNum       number corresponding to team
   * @param configuration properly initialized Stream with data to initialize Piece
   */
  public DynamicPiece(char identifier, int teamNum, Stream configuration) {
    super(identifier, teamNum, configuration);
    hasMoved = false;
  }

  /**
   * creates default DynamicPiece used for easy testing
   */
  public DynamicPiece() {
    super();
  }

  /**
   * @return true if piece has moved, false othersie
   */
  public boolean getHasMoved() {
    return hasMoved;
  }

  /**
   * sets hasMoved to true when called, since this piece has now been moved
   */
  public void moved() {
    if (!hasMoved) {
      hasMoved = true;
    }
  }


}
