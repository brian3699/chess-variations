package ooga.model.util;

import org.json.JSONObject;

public class Stream {

  private char id;
  private JSONObject data;

  /**
   * creates a stream based on key and input
   *
   * @param key   value that specifies type of Piece
   * @param input holds all properties of that piece
   */
  public Stream(char key, JSONObject input) {
    id = key;
    data = input;
  }

  /**
   * @return id for this Stream
   */
  public char getId() {
    return id;
  }

  /**
   * @return data for this Stream
   */
  public JSONObject getData() {
    return data;
  }
}
