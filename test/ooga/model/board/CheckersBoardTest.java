package ooga.model.board;

import static org.junit.jupiter.api.Assertions.*;

import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.IOException;
import javax.management.ReflectionException;
import ooga.util.CSVParser;
import ooga.util.CSVParserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckersBoardTest {
  Board cBoard2;
  CSVParserInterface parser4;
  Board cBoard;
  Board cBoard3;
  CSVParserInterface parser3;
  CSVParserInterface parser;
  @BeforeEach
  void setUp()
      throws CsvValidationException, IOException, ReflectionException{
    cBoard2= new CheckersBoard();
    parser4 = new CSVParser();
    parser4.readCSVFile(new File("data/boards/Checkers_twoMove_board.csv"));
    cBoard2.initialize(parser4.getInitialStates(), "ooga/model/resources/checkers.properties");
    cBoard= new CheckersBoard();
    parser3 = new CSVParser();
    parser3.readCSVFile(new File("data/boards/Checkers_oneMove_board.csv"));
    cBoard3 = new CheckersBoard();
    cBoard.initialize(parser3.getInitialStates(), "ooga/model/resources/checkers.properties");
    parser = new CSVParser();
    parser.readCSVFile(new File("data/boards/Checkers_board.csv"));
    cBoard3.initialize(parser.getInitialStates(), "ooga/model/resources/checkers.properties");
  }
  @Test
  void findValidMoves() {
    cBoard.findValidMoves(3,0);
    assertEquals(1, cBoard.getValidMoves().get(0).deltaRow());
    assertEquals(2, cBoard.getValidMoves().get(0).deltaCol());
    assertEquals(1, cBoard.getValidMoves().size());

    cBoard2.findValidMoves(4,2);
    assertEquals(3, cBoard2.getValidMoves().get(0).deltaRow());
    assertEquals(3, cBoard2.getValidMoves().get(0).deltaCol());
    assertEquals(1, cBoard2.getValidMoves().size());

    cBoard2.findValidMoves(2,4);
    assertEquals(3, cBoard2.getValidMoves().get(0).deltaRow());
    assertEquals(5, cBoard2.getValidMoves().get(0).deltaCol());
    assertEquals(3, cBoard2.getValidMoves().get(1).deltaRow());
    assertEquals(3, cBoard2.getValidMoves().get(1).deltaCol());
    assertEquals(2, cBoard2.getValidMoves().size());


    cBoard2.findValidMoves(5,5);
    assertEquals(6, cBoard2.getValidMoves().get(0).deltaRow());
    assertEquals(6, cBoard2.getValidMoves().get(0).deltaCol());
    assertEquals(6, cBoard2.getValidMoves().get(1).deltaRow());
    assertEquals(4, cBoard2.getValidMoves().get(1).deltaCol());
    assertEquals(4, cBoard2.getValidMoves().get(2).deltaRow());
    assertEquals(6, cBoard2.getValidMoves().get(2).deltaCol());
    assertEquals(4, cBoard2.getValidMoves().get(3).deltaRow());
    assertEquals(4, cBoard2.getValidMoves().get(3).deltaCol());
    assertEquals(4, cBoard2.getValidMoves().size());

    cBoard3.findValidMoves(0,1);
    assertEquals(0, cBoard3.getValidMoves().size());
  }

  @Test
  void movePiece() {
    cBoard2.setCurrentPiece(cBoard2.getPieceAt(2,2));
    cBoard2.setCurrentRow(2);
    cBoard2.setCurrentCol(2);
    cBoard2.movePiece(4,0);
    assertNull(cBoard2.getPieceAt(2,2));
    assertNull(cBoard2.getPieceAt(3,1));
    assertNotNull(cBoard2.getPieceAt(4,0));


    cBoard.setCurrentPiece(cBoard.getPieceAt(3,0));
    cBoard.setCurrentRow(3);
    cBoard.setCurrentCol(0);
    cBoard.movePiece(1,2);
    assertNull(cBoard.getPieceAt(3,0));
    assertNotNull(cBoard.getPieceAt(1,2));
  }


  @Test
  void hasWon()
      throws ReflectionException{
    assertEquals(-1, cBoard.getHasWon());
    cBoard.findValidMoves(3,0);
    cBoard.movePiece(1,2);
    assertEquals(1,cBoard.getHasWon());


    assertEquals(-1, cBoard2.getHasWon());
    cBoard2.findValidMoves(4,2);
    cBoard2.movePiece(3,3);
    cBoard2.setActiveTeam();
    cBoard2.findValidMoves(2,2);
    cBoard2.movePiece(4,0);
    ((CheckersBoard)cBoard2).setTurnOver(false);
    cBoard2.findValidMoves(2,4);
    cBoard2.movePiece(4,2);
    assertEquals(2,cBoard2.getHasWon());
  }

  @Test
  void updateTeamVals() {
    cBoard.findValidMoves(3,0);
    cBoard.movePiece(1,2);
    assertEquals(1, cBoard.getTeam(1).getScore());
    cBoard2.findValidMoves(4,2);
    cBoard2.movePiece(3,3);
    cBoard2.setActiveTeam();
    cBoard2.findValidMoves(2,2);
    cBoard2.movePiece(4,0);
    ((CheckersBoard)cBoard2).setTurnOver(false);
    cBoard2.findValidMoves(2,4);
    cBoard2.movePiece(4,2);
    assertEquals(2, cBoard2.getTeam(2).getScore());

  }

  @Test
  void setTurnOver(){
    ((CheckersBoard)cBoard2).setTurnOver(true);
    assertTrue(((CheckersBoard) cBoard2).getTurnOver());
    ((CheckersBoard)cBoard).setTurnOver(false);
    assertFalse(((CheckersBoard) cBoard).getTurnOver());
  }

  @Test
  void setAndGetTurnOver() {
    cBoard.findValidMoves(3,0);
    cBoard.movePiece(1,2);
    cBoard2.findValidMoves(4,2);
    cBoard2.movePiece(3,3);
    ((CheckersBoard)cBoard2).setIfTurnOver(3,3);
    assertTrue(((CheckersBoard)cBoard2).getTurnOver());
    cBoard2.findValidMoves(2,4);
    cBoard2.movePiece(4,2);
    ((CheckersBoard)cBoard2).setIfTurnOver(4,2);
    assertTrue(((CheckersBoard)cBoard2).getTurnOver());
  }


  @Test
  void getJumped() {
    cBoard.findValidMoves(3,0);
    cBoard.movePiece(1,2);
    assertTrue(((CheckersBoard)cBoard).getJumped());
    cBoard2.findValidMoves(2,4);
    cBoard2.movePiece(4,2);
    ((CheckersBoard)cBoard2).setIfTurnOver(4,2);
    assertTrue(((CheckersBoard)cBoard2).getJumped());
  }
}