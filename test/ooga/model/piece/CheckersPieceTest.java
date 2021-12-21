package ooga.model.piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import javax.management.ReflectionException;
import ooga.model.util.Layout;
import ooga.model.util.Move;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class CheckersPieceTest extends DukeApplicationTest {
  @Test
  void testIsValidMove(){
    CheckersPiece p = new CheckersPiece();
    List<List<PieceInterface>> boardPieces = new ArrayList<>();
    for(int i = 0; i < 8; i++){
      boardPieces.add(new ArrayList<>());
      for(int j = 0; j < 8; j++){
        boardPieces.get(i).add(new CheckersPiece());
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
  void testConstructorRegularPiece() throws ReflectionException {
    CheckersPiece p = (CheckersPiece) makePieceFromStream(1, 'C', null, null, "ooga/model/resources/checkers.properties");
    assertNotNull(p);
    assertEquals(p.getKey(), 'C');
    assertEquals(p.getTeam(), 1);
    assertEquals(p.getPoints(), 1);
    List<Move> possible = List.of(new Move("(-1,1)"), new Move("(-1,-1)"));
    assertEquals(possible.size(), p.getPossibleMoves().size());
    for(Move m : possible)
      assertTrue(p.getPossibleMoves().contains(m));
    List<Move> capture = List.of(new Move("(-2,2)"), new Move("(-2,-2)"));
    assertEquals(capture.size(), p.getCaptureMoves().size());
  }
  @Test
  void testConstructorKing() throws ReflectionException {
    CheckersPiece p = (CheckersPiece) makePieceFromStream(1, 'X', null, null, "ooga/model/resources/checkers.properties");
    assertNotNull(p);
    assertEquals(p.getKey(), 'X');
    assertEquals(p.getTeam(), 1);
    assertEquals(p.getPoints(), 1);
    List<Move> possible = List.of(new Move("(-1,1)"), new Move("(-1,-1)"), new Move("(1,-1)"), new Move("(1,1)"));
    assertEquals(possible.size(), p.getPossibleMoves().size());
    for(Move m : possible)
      assertTrue(p.getPossibleMoves().contains(m));
    List<Move> capture = List.of(new Move("(-2,2)"), new Move("(-2,-2)"), new Move("(2,-2)"), new Move("(2,2)"));
    assertEquals(capture.size(), p.getCaptureMoves().size());
  }
}
