package ooga.model.util;

public class Move {

  private static final String OPEN = "(";
  private static final String CLOSE = ")";
  private static final String COMMA = ",";

  private int dr;
  private int dc;

  /**
   * creates a default move with x and y defaulted to 0
   */
  public Move() {
    dr = 0;
    dc = 0;
  }

  /**
   * creates a move with specified x and y values
   *
   * @param r is the dx value for this move
   * @param c is the dy value for this move
   */
  public Move(int r, int c) {
    dr = r;
    dc = c;
  }

  /**
   * creates a move from a formatted string with specified x and y values
   *
   * @param s is a string containing x and y values, formatted as "(X,Y)"
   */
  public Move(String s) {
    dr = Integer.parseInt(s.substring(s.indexOf(OPEN) + 1, s.indexOf(COMMA)));
    dc = Integer.parseInt(s.substring(s.indexOf(COMMA) + 1, s.indexOf(CLOSE)));
  }

  /**
   * returns a string of form (X,Y)
   *
   * @return a string representation of this move
   */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(OPEN).append(dr).append(COMMA).append(dc).append(CLOSE);
    return s.toString();
  }

  /**
   * get x value of this move
   *
   * @return dx
   */
  public int deltaRow() {
    return dr;
  }

  /**
   * get y value of this move
   *
   * @return dy
   */
  public int deltaCol() {
    return dc;
  }

  /**
   * determines if this move is the same as the passed in object o
   *
   * @param o is the object we want to compare this to
   * @return true if this move is the same as o, false otherwise
   */
  public boolean equals(Object o) {
    if (!(o instanceof Move)) {
      return false;
    }
    Move m = (Move) o;
    return m.deltaRow() == deltaRow() && m.deltaCol() == deltaCol();
  }

  /**
   * multiply two moves element-wise
   *
   * @param multiplicand is the Move to be multiplied with this Move
   * @return product of this and multiplicand
   */
  public Move multiply(Move multiplicand) {
    return new Move(this.dr * multiplicand.dr, this.dc * multiplicand.dc);
  }

  /**
   * switches row and column
   *
   * @return a new Move with row and column switched from this Move
   */
  public Move swapRowCol() {
    return new Move(this.deltaCol(), this.deltaRow());
  }

}
