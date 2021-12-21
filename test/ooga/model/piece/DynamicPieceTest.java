package ooga.model.piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.management.ReflectionException;
import ooga.model.util.Move;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class DynamicPieceTest extends DukeApplicationTest {
  @Test
  void testMoved(){
    DynamicPiece d = new DynamicPiece();
    assertFalse(d.getHasMoved());
    d.moved();
    assertTrue(d.getHasMoved());
  }
  @Test
  void testMakeRook() throws ReflectionException {
    PieceInterface p = makePieceFromStream(4, 'R', null, null, "ooga/model/resources/chess.properties");
    assertNotNull(p);
    assertEquals(p.getKey(), 'R');
    assertEquals(p.getTeam(), 4);
    assertEquals(p.getPoints(), 5);
    assertEquals(p.getMoveIterations(), 1);
    List<Move> possible = List.of(new Move("(0,1)"), new Move("(0,-1)"), new Move("(1,0)"), new Move("(-1,0)"));
    assertEquals(possible.size(), p.getPossibleMoves().size());
    for(Move m : possible)
      assertTrue(p.getPossibleMoves().contains(m));
  }

}
