package ooga.model.piece;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javafx.stage.Stage;
import javax.management.ReflectionException;
import ooga.model.board.ChessBoard;
import ooga.model.util.Layout;
import ooga.model.util.Move;
import ooga.model.util.Stream;
import org.json.JSONObject;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessPieceTest extends DukeApplicationTest {

  @Override
  public void start(Stage stage) {

  }

  @Test
  void testMakeQueen() throws ReflectionException {
    PieceInterface p = makePieceFromStream(3, 'Q', null, null, "ooga/model/resources/chess.properties");
    assertNotNull(p);
    assertEquals(p.getKey(), 'Q');
    assertEquals(p.getTeam(), 3);
    assertEquals(p.getPoints(), 9);
    assertEquals(p.getMoveIterations(), 1);
    List<Move> possible = List.of(new Move("(0,1)"), new Move("(0,-1)"), new Move("(1,1)"), new Move("(1,-1)")
        , new Move("(1,0)"), new Move("(-1,0)"), new Move("(-1,1)"), new Move("(1,-1)"));
    assertEquals(possible.size(), p.getPossibleMoves().size());
    for(Move m : possible)
      assertTrue(p.getPossibleMoves().contains(m));
  }

  @Test
  void testMakeKnight() throws ReflectionException {
    PieceInterface p = makePieceFromStream(-1, 'N', null, null, "ooga/model/resources/chess.properties");
    assertNotNull(p);
    assertEquals(p.getKey(), 'N');
    assertEquals(p.getTeam(), -1);
    assertEquals(p.getPoints(), 3);
    assertEquals(p.getMoveIterations(), 0);
    List<Move> possible = List.of(new Move("(-2,-1)"), new Move("(-2,1)"), new Move("(-1,2)"), new Move("(1,2)")
        , new Move("(2,1)"), new Move("(2,-1)"), new Move("(-1,-2)"), new Move("(1,-2)"));
    assertEquals(possible.size(), p.getPossibleMoves().size());
    for(Move m : possible)
      assertTrue(p.getPossibleMoves().contains(m));
  }
  @Test
  void testMakeBishop() throws ReflectionException {
    PieceInterface p = makePieceFromStream(-10, 'B', null, null, "ooga/model/resources/chess.properties");
    assertNotNull(p);
    assertEquals(p.getKey(), 'B');
    assertEquals(p.getTeam(), -10);
    assertEquals(p.getPoints(), 3);
    assertEquals(p.getMoveIterations(), 1);
    List<Move> possible = List.of(new Move("(1,1)"), new Move("(1,-1)"), new Move("(-1,1)"), new Move("(-1,-1)"));
    assertEquals(possible.size(), p.getPossibleMoves().size());
    for(Move m : possible)
      assertTrue(p.getPossibleMoves().contains(m));
  }
  @Test
  void testIsValidMoveAllEmpty() throws ReflectionException {
    PieceInterface p = makePieceFromStream(-10, 'B', null, null, "ooga/model/resources/chess.properties");
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
    PieceInterface p = makePieceFromStream(-10, 'B', null, null, "ooga/model/resources/chess.properties");
    List<List<PieceInterface>> boardPieces = new ArrayList<>();
    for(int i = 0; i < 8; i++){
      boardPieces.add(new ArrayList<>());
      for(int j = 0; j < 8; j++){
        boardPieces.get(i).add(new ChessPiece());
      }
    }
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        Layout l = new Layout(i, j, boardPieces);
        assertFalse(p.isValidMove(i, j, l));
      }
    }
  }

}

