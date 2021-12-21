package ooga.model.piece;

import ooga.model.util.Stream;

public class KingPiece extends DynamicPiece {

  private boolean check;

  /**
   * creates KingPiece and initializes all appropriate values
   *
   * @param identifier    key into the properties file for the piece type
   * @param teamNum       number corresponding to team
   * @param configuration properly initialized Stream with data to initialize Piece
   */
  public KingPiece(char identifier, int teamNum, Stream configuration) {
    super(identifier, teamNum, configuration);
    check = false;
  }

  /**
   * @return true if this king is in check, false otherwise
   */
  public boolean isInCheck() {
    return check;
  }

  /**
   * sets king to be in check
   */
  public void setInCheck() {
    check = true;
  }

  /**
   * sets king to be not in check
   */
  public void setNotInCheck() {
    check = false;
  }
}
