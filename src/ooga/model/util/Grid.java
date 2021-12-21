package ooga.model.util;


public class Grid {

  private String[][] gridVals;

  /**
   * grid object that holds the values of the game grid
   *
   * @param vals 2d array of grid values
   */
  public Grid(String[][] vals) {
    gridVals = vals;
  }

  /**
   * get the value of the grid at a specific x,y position
   *
   * @param x position of value
   * @param y position of value
   * @return String of the value at that position
   */
  public String getVal(int x, int y) {
    return gridVals[x][y];
  }

  /**
   * get lenght of grid
   *
   * @return int of length
   */
  public int length() {
    return gridVals.length;
  }

  /**
   * get width of grid
   *
   * @return int of width
   */
  public int width() {
    return gridVals[0].length;
  }

  /**
   * get row of grid
   *
   * @param x coordinate of the row
   * @return array of a row
   */
  public String[] get(int x) {
    return gridVals[x];
  }


}
