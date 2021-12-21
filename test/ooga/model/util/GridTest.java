package ooga.model.util;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

public class GridTest extends DukeApplicationTest {
  @Test
  void testConstructor() {
    String[][] data = {
        {"A", "B", "C", "D"}, {"E", "F", "G", "H"}
    };
    Grid g = new Grid(data);
    assertNotNull(g);
  }
  @Test
  void testGetVal() {
    String[][] data = {
        {"A", "B", "C", "D"}, {"E", "F", "G", "H"}
    };
    Grid g = new Grid(data);
    assertEquals(g.getVal(0, 0), "A");
    assertEquals(g.getVal(1, 2), "G");
  }
  @Test
  void testLength() {
    String[][] data = {
        {"A", "B", "C", "D"}, {"E", "F", "G", "H"}
    };
    Grid g = new Grid(data);
    assertEquals(g.length(), 2);
  }
  @Test
  void testWidth() {
    String[][] data = {
        {"A", "B", "C", "D"}, {"E", "F", "G", "H"}
    };
    Grid g = new Grid(data);
    assertEquals(g.width(), 4);
  }
  @Test
  void testGet() {
    String[][] data = {
        {"A", "B", "C", "D"}, {"E", "F", "G", "H"}
    };
    Grid g = new Grid(data);
    String[] row0 = {"A", "B", "C", "D"};
    String[] row1 = {"E", "F", "G", "H"};
    assertTrue(Arrays.equals(g.get(0), row0));
    assertTrue(Arrays.equals(g.get(1), row1));
  }
}
