package ooga.model.team;

import java.util.ArrayList;
import java.util.HashMap;
import ooga.model.piece.ChessPiece;
import ooga.model.piece.Piece;
import ooga.model.piece.PieceInterface;
import ooga.model.team.Team;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TeamTest extends DukeApplicationTest {

  @Test
  void testConstructor(){
    Team t = new Team(1);
    assertNotNull(t);
    assertEquals(t.getScore(), 0);
    assertEquals(t.getActive(), new ArrayList());
    assertEquals(t.getCaptured(), new HashMap<>());
  }
  @Test
  void testAddActive(){
    Team t = new Team(3);
    PieceInterface p = new ChessPiece();
    t.setActivePiece(p);
    assertTrue(t.getActive().contains(p));
    PieceInterface b = new ChessPiece();
    t.setActivePiece(b);
    assertTrue(t.getActive().contains(b));
    assertTrue(t.getActive().contains(p));
  }
  @Test
  void testSetCaptured(){
    Team t = new Team(0);
    PieceInterface p = new ChessPiece();
    p.setPoints(5);
    t.setCaptured(p.getTeam(), p);
    assertTrue(t.getCaptured().get(p.getTeam()).contains(p));
    assertEquals(t.getScore(), 5);
    PieceInterface b = new ChessPiece();
    b.setPoints(10);
    b.setTeam(1);
    t.setCaptured(b.getTeam(), b);
    assertTrue(t.getCaptured().get(b.getTeam()).contains(b));
    assertEquals(t.getScore(), 15);
  }
  @Test
  void testGetTeamNum(){
    Team t = new Team(5);
    assertEquals(5, t.getTeamNum());
  }
  @Test
  void testRemoveActive(){
    Team t = new Team(0);
    PieceInterface p = new ChessPiece();
    PieceInterface b = new ChessPiece();
    t.setActivePiece(p);
    t.setActivePiece(b);
    t.removeActive(p);
    assertTrue(!t.getActive().contains(p));
    assertTrue(t.getActive().contains(b));
    t.removeActive(b);
    assertTrue(!t.getActive().contains(b));
  }
  @Test
  void testGetTeamSize(){
    Team t = new Team(0);
    PieceInterface p = new ChessPiece();
    PieceInterface b = new ChessPiece();
    t.setActivePiece(p);
    assertEquals(t.getSizeOfTeam(), 1);
    t.setActivePiece(b);
    assertEquals(t.getSizeOfTeam(), 2);
    t.removeActive(p);
    t.removeActive(b);
    assertEquals(t.getSizeOfTeam(), 0);
  }
}

