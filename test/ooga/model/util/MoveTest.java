package ooga.model.util;

import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveTest extends DukeApplicationTest {

  @Test
  void testDefaultConstructor(){
    Move m = new Move();
    assertNotNull(m);
    assertEquals(m.deltaRow(), 0);
    assertEquals(m.deltaCol(), 0);
  }
  @Test
  void testIntConstructor(){
    Move m = new Move(5, 8);
    assertNotNull(m);
    assertEquals(m.deltaRow(), 5);
    assertEquals(m.deltaCol(), 8);
    m = new Move(-7, 3);
    assertNotNull(m);
    assertEquals(m.deltaRow(), -7);
    assertEquals(m.deltaCol(), 3);
  }
  @Test
  void testStringConstructor(){
    Move m = new Move("(-8,-5)");
    assertNotNull(m);
    assertEquals(m.deltaRow(), -8);
    assertEquals(m.deltaCol(), -5);
  }
  @Test
  void testToString(){
    Move m1 = new Move();
    Move m2 = new Move(0, 0);
    assertEquals(m1.toString(), m2.toString());
    m1 = new Move(-3, 3);
    m2 = new Move(3, 3);
    assertNotEquals(m1.toString(), m2.toString());
    m2 = new Move(-3, 3);
    assertEquals(m1.toString(), m2.toString());
    String s = "(-3,3)";
    assertEquals(m2.toString(), s);
    s = "(4,4)";
    m2 = new Move(4, 4);
    assertEquals(m2.toString(), s);

  }
  @Test
  void testEquals(){
    Move m1 = new Move();
    Move m2 = new Move(0, 0);
    assertTrue(m1.equals(m2));
    m2 = new Move(-1, 1);
    assertFalse(m1.equals(m2));
  }
  @Test
  void testEqualsWrongType(){
    Move m = new Move();
    String s = new String();
    assertFalse(m.equals(s));
  }
  @Test
  void testMultiply(){
    Move m1 = new Move();
    Move m2 = new Move();
    assertEquals(m1.multiply(m2), new Move());
    m1 = new Move(5, 3);
    m2 = new Move(2, 6);
    assertEquals(m1.multiply(m2), new Move(m1.deltaRow() * m2.deltaRow(), m1.deltaCol() * m2.deltaCol()));
    m2 = m2.multiply(new Move(-1, -1));
    assertEquals(m1.multiply(m2), m2.multiply(m1));
    assertEquals(m1.multiply(m2), new Move(m1.deltaRow() * m2.deltaRow(), m1.deltaCol() * m2.deltaCol()));
  }
  @Test
  void testSwapRowCol(){
    Move m = new Move(1, 2);
    int r = m.deltaRow();
    int c = m.deltaCol();
    Move s = m.swapRowCol();
    assertEquals(s.deltaRow(), c);
    assertEquals(s.deltaCol(), r);
    m = new Move(-3, 5);
    r = m.deltaRow();
    c = m.deltaCol();
    s = m.swapRowCol();
    assertEquals(s.deltaRow(), c);
    assertEquals(s.deltaCol(), r);
  }

}

