package ooga.model.piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import javax.management.ReflectionException;
import ooga.model.util.Direction;
import ooga.model.util.Layout;
import ooga.model.util.Move;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class PawnPieceTest extends DukeApplicationTest {

  @Test
  void testMakePawn() throws ReflectionException {
    PieceInterface p = makePieceFromStream(1, 'P', null, null,
        "ooga/model/resources/chess.properties");
    assertNotNull(p);
    assertEquals(p.getKey(), 'P');
    assertEquals(p.getTeam(), 1);
    assertEquals(p.getPoints(), 1);
    assertEquals(p.getMoveIterations(), 0);
    List<Move> possible = List.of(new Move("(-1,0)"), new Move("(-1,1)"), new Move("(-1,-1)"), new Move("(-2,0)"));
    assertEquals(possible.size(), p.getPossibleMoves().size());
    for(Move m : possible)
      assertTrue(p.getPossibleMoves().contains(m));
  }
  @Test
  void testFindDirection() throws ReflectionException {
    PieceInterface p = makePieceFromStream(1, 'P', null, null,
        "ooga/model/resources/chess.properties");
    assertEquals(p.findDirection(), Direction.UP);
    p.setTeam(2);
    assertEquals(p.findDirection(), Direction.DOWN);
    p.setTeam(3);
    assertEquals(p.findDirection(), Direction.RIGHT);
    p.setTeam(4);
    assertEquals(p.findDirection(), Direction.LEFT);
  }

  @Test
  void testIsValidMoveAllEmpty() throws ReflectionException {
    PieceInterface p = makePieceFromStream(1, 'P', null, null, "ooga/model/resources/chess.properties");
    List<List<PieceInterface>> boardPieces = new ArrayList<>();
    for(int i = 0; i < 8; i++){
      boardPieces.add(new ArrayList<>());
      for(int j = 0; j < 8; j++){
        boardPieces.get(i).add(null);
      }
    }
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        Layout l = new Layout(i, j, boardPieces);
        assertTrue(p.isValidMove(i, j, l));
      }
    }
  }
  @Test
  void testIsValidMoveAllFull() throws ReflectionException {
    PieceInterface p = makePieceFromStream(2, 'P', null, null, "ooga/model/resources/chess.properties");
    List<List<PieceInterface>> boardPieces = new ArrayList<>();
    for(int i = 0; i < 8; i++){
      boardPieces.add(new ArrayList<>());
      for(int j = 0; j < 8; j++){
        ChessPiece x = new ChessPiece();
        x.setTeam(2);
        boardPieces.get(i).add(x);
      }
    }
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        Layout l = new Layout(i, j, boardPieces);
        assertFalse(p.isValidMove(i, j, l));
      }
    }
  }
  @Test
  void testMoved() throws ReflectionException {
    PawnPiece p = (PawnPiece)makePieceFromStream(2, 'P', null, null, "ooga/model/resources/chess.properties");
    assertFalse(p.getHasMoved());
    assertEquals(p.getPossibleMoves().size(), 4);
    p.moved();
    assertTrue(p.getHasMoved());
    assertEquals(p.getPossibleMoves().size(), 3);

  }

}
