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

public class KingPieceTest extends DukeApplicationTest {
  @Test
  void testConstructor() throws ReflectionException {
    KingPiece p = (KingPiece) makePieceFromStream(1, 'K', null, null, "ooga/model/resources/chess.properties");
    assertNotNull(p);
    assertEquals(p.getKey(), 'K');
    assertEquals(p.getTeam(), 1);
    assertEquals(p.getPoints(), 4);
    assertEquals(p.getMoveIterations(), 0);
    List<Move> possible = List.of(new Move("(0,1)"), new Move("(0,-1)"), new Move("(1,1)"), new Move("(1,-1)")
        , new Move("(1,0)"), new Move("(-1,0)"), new Move("(-1,1)"), new Move("(1,-1)"));
    assertEquals(possible.size(), p.getPossibleMoves().size());
    for(Move m : possible)
      assertTrue(p.getPossibleMoves().contains(m));
  }
  @Test
  void testCheckMethods() throws ReflectionException {
    KingPiece p = (KingPiece) makePieceFromStream(1, 'K', null, null, "ooga/model/resources/chess.properties");
    assertFalse(p.isInCheck());
    p.setInCheck();
    assertTrue(p.isInCheck());
    p.setNotInCheck();
    assertFalse(p.isInCheck());
  }
}
