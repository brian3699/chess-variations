package ooga.model.board;

import static org.junit.jupiter.api.Assertions.*;

import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.management.ReflectionException;
import ooga.util.CSVParser;
import ooga.util.CSVParserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChessBoardTest {
  public Board board;
  public Board board2;
  public Board board3;
  public Board board4;
  @BeforeEach
  void setUp()
      throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException {
    CSVParserInterface parser = new CSVParser();
    CSVParserInterface parser2 = new CSVParser();
    CSVParserInterface parser3 = new CSVParser();
    CSVParserInterface parser4 = new CSVParser();
    board = new ChessBoard();
    board2 = new ChessBoard();
    board3= new ChessBoard();
    board4= new ChessBoard();
    parser.readCSVFile(new File("data/boards/Chess_board.csv"));
    parser2.readCSVFile(new File("data/boards/BigBoardChess_board.csv"));
    parser3.readCSVFile(new File("data/boards/ChristmasChess_board.csv"));
    parser4.readCSVFile(new File("data/boards/Chess_board_oneMove.csv"));
    board.initialize(parser.getInitialStates(), "ooga/model/resources/chess.properties");
    board2.initialize(parser2.getInitialStates(),"ooga/model/resources/chess.properties");
    board3.initialize(parser3.getInitialStates(), "ooga/model/resources/chess.properties");
    board4.initialize(parser4.getInitialStates(), "ooga/model/resources/chess.properties");
  }
  @Test
  void findValidMoves() {
    board.findValidMoves(1,0);
    assertEquals(3, board.getValidMoves().get(0).deltaRow());
    assertEquals(0, board.getValidMoves().get(0).deltaCol());
    assertEquals(2, board.getValidMoves().get(1).deltaRow());
    assertEquals(0, board.getValidMoves().get(1).deltaCol());
    assertEquals(2, board.getValidMoves().size());
    board.findValidMoves(0,1);
    assertEquals(2, board.getValidMoves().get(0).deltaRow());
    assertEquals(2, board.getValidMoves().get(0).deltaCol());
    assertEquals(2, board.getValidMoves().get(1).deltaRow());
    assertEquals(0, board.getValidMoves().get(1).deltaCol());
    assertEquals(2, board.getValidMoves().size());
    board2.findValidMoves(1,1);
    assertEquals(0, board2.getValidMoves().get(0).deltaRow());
    assertEquals(1, board2.getValidMoves().get(0).deltaCol());
    assertEquals(1, board2.getValidMoves().get(1).deltaRow());
    assertEquals(0, board2.getValidMoves().get(1).deltaCol());
    assertEquals(2, board2.getValidMoves().size());
    board2.findValidMoves(1,1);
    assertEquals(0, board2.getValidMoves().get(0).deltaRow());
    assertEquals(1, board2.getValidMoves().get(0).deltaCol());
    assertEquals(1, board2.getValidMoves().get(1).deltaRow());
    assertEquals(0, board2.getValidMoves().get(1).deltaCol());
    assertEquals(2, board2.getValidMoves().size());
    board2.findValidMoves(1,3);
    assertEquals(0, board2.getValidMoves().get(0).deltaRow());
    assertEquals(2, board2.getValidMoves().get(0).deltaCol());
    assertEquals(0, board2.getValidMoves().get(1).deltaRow());
    assertEquals(4, board2.getValidMoves().get(1).deltaCol());
    assertEquals(2, board2.getValidMoves().size());
    board2.findValidMoves(1,5);
    assertEquals(0, board2.getValidMoves().get(0).deltaRow());
    assertEquals(4, board2.getValidMoves().get(0).deltaCol());
    assertEquals(0, board2.getValidMoves().get(1).deltaRow());
    assertEquals(5, board2.getValidMoves().get(1).deltaCol());
    assertEquals(0, board2.getValidMoves().get(2).deltaRow());
    assertEquals(6, board2.getValidMoves().get(2).deltaCol());
    assertEquals(3, board2.getValidMoves().size());
    board3.setCurrentPiece(board3.getPieceAt(8,3));
    board3.setCurrentCol(3);
    board3.setCurrentRow(8);
    board3.movePiece(7,3);
    board3.findValidMoves(9,4);
    assertEquals(8, board3.getValidMoves().get(0).deltaRow());
    assertEquals(3, board3.getValidMoves().get(0).deltaCol());
    assertEquals(7, board3.getValidMoves().get(1).deltaRow());
    assertEquals(2, board3.getValidMoves().get(1).deltaCol());
    assertEquals(6, board3.getValidMoves().get(2).deltaRow());
    assertEquals(1, board3.getValidMoves().get(2).deltaCol());
    assertEquals(5, board3.getValidMoves().get(3).deltaRow());
    assertEquals(0, board3.getValidMoves().get(3).deltaCol());
    assertEquals(4, board3.getValidMoves().size());
  }


  @Test
  void movePiece() {
    board3.setCurrentPiece(board3.getPieceAt(6,5));
    board3.setCurrentCol(5);
    board3.setCurrentRow(6);
    board3.movePiece(4,5);
    assertNull(board3.getPieceAt(6,5));
    assertNotNull(board3.getPieceAt(4,5));
    board3.setActiveTeam();
    board3.setCurrentPiece(board3.getPieceAt(3,4));
    board3.setCurrentCol(4);
    board3.setCurrentRow(3);
    board3.movePiece(4,5);
    assertNull(board3.getPieceAt(3,4));
    assertNotNull(board3.getPieceAt(4,5));
    assertEquals(1, board3.getActiveTeam().getCaptured().size());
  }

  @Test
  void hasWon() {
    assertEquals(-1, board4.getHasWon());
    board4.setCurrentPiece(board4.getPieceAt(6,0));
    board4.setCurrentCol(0);
    board4.setCurrentRow(6);
    board4.movePiece(5,0);
    board4.setActiveTeam();
    board4.setCurrentPiece(board4.getPieceAt(1,3));
    board4.setCurrentCol(3);
    board4.setCurrentRow(1);
    board4.movePiece(2,1);
    assertEquals(2, board4.getHasWon());
  }
}