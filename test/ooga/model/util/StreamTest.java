package ooga.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class StreamTest extends DukeApplicationTest {

  @Test
  void testConstructor(){
    Stream s = new Stream('l', new JSONObject());
    assertNotNull(s);
    assertNotNull(s.getId());
    assertNotNull(s.getData());
  }

  @Test
  void testGetId(){
    char id = 'x';
    Stream s = new Stream(id, new JSONObject());
    assertEquals(s.getId(), id);
  }

  @Test
  void testGetData(){
    char key = 'x';
    JSONObject j = new JSONObject(key);
    Stream s = new Stream(key, j);
    assertEquals(s.getData(), j);
  }

}
