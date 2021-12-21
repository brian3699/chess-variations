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

class BoardTest {
  public Board board;
  public Board board2;
  public Board cBoard;
  public Board checkBoard1;
  public Board checkBoard2;
  public Board pawnBoard1;
  public Board pawnBoard2;
  public Board castleBoard1;
  public Board castleBoard2;
  public Board castleRight1;
  public Board castleRight2;
  public Board castleLeft1;
  public Board castleLeft2;
  public CSVParserInterface parser;
  public CSVParserInterface parser2;
  public CSVParserInterface parser3;
  public CSVParserInterface parserCheck1;
  public CSVParserInterface parserCheck2;
  public CSVParserInterface parserPawn1;
  public CSVParserInterface parserPawn2;
  public CSVParserInterface parserCastle1;
  public CSVParserInterface parserCastle2;
  public CSVParserInterface parserCastleRight1;
  public CSVParserInterface parserCastleRight2;
  public CSVParserInterface parserCastleLeft1;
  public CSVParserInterface parserCastleLeft2;
  public List<List<String>> list1= new ArrayList<List<String>>();
  public List<List<String>> list2= new ArrayList<List<String>>();
  public List<List<String>> list3= new ArrayList<List<String>>();
  @BeforeEach
  void setUp()
          throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException {
    parser = new CSVParser();
    parser2 = new CSVParser();
    parser3 = new CSVParser();
    parserCheck1 = new CSVParser();
    parserCheck2 = new CSVParser();
    parserPawn1 = new CSVParser();
    parserPawn2 = new CSVParser();
    parserCastle1 = new CSVParser();
    parserCastle2 = new CSVParser();
    parserCastleRight1 = new CSVParser();
    parserCastleRight2 = new CSVParser();
    parserCastleLeft1= new CSVParser();
    parserCastleLeft2= new CSVParser();

    board = new ChessBoard();
    board2 = new ChessBoard();
    cBoard= new CheckersBoard();
    checkBoard1 = new ChessBoard();
    checkBoard2 = new ChessBoard();
    pawnBoard1 = new ChessBoard();
    pawnBoard2 = new ChessBoard();
    castleBoard1= new ChessBoard();
    castleBoard2= new ChessBoard();
    castleRight1= new ChessBoard();
    castleRight2= new ChessBoard();
    castleLeft1= new ChessBoard();
    castleLeft2= new ChessBoard();

    parser.readCSVFile(new File("data/boards/Chess_board.csv"));
    parser2.readCSVFile(new File("data/boards/ChristmasChess_board.csv"));
    parser3.readCSVFile(new File("data/boards/Checkers_board.csv"));
    parserCheck1.readCSVFile(new File("data/boards/Board_In_Check1.csv"));
    parserCheck2.readCSVFile(new File("data/boards/Board_In_Check2.csv"));
    parserPawn1.readCSVFile(new File("data/boards/Pawn_Can_Promote1.csv"));
    parserPawn2.readCSVFile(new File("data/boards/Pawn_Can_Promote2.csv"));
    parserCastle1.readCSVFile(new File("data/boards/Castle_Possible_1.csv"));;
    parserCastle2.readCSVFile(new File("data/boards/Castle_Possible_2.csv"));;
    parserCastleRight1.readCSVFile(new File("data/boards/Castle_Right_1.csv"));;
    parserCastleRight2.readCSVFile(new File("data/boards/Castle_Right_2.csv"));;
    parserCastleLeft1.readCSVFile(new File("data/boards/Castle_Left_1.csv"));;
    parserCastleLeft2.readCSVFile(new File("data/boards/Castle_Left_2.csv"));;

    list1.add(Arrays.asList("R2","N2","B2","Q2","K2","B2","N2","R2"));
    list1.add(Arrays.asList("P2","P2","P2","P2","P2","P2","P2","P2"));
    list1.add(Arrays.asList("-","-","-","-","-","-","-","-"));
    list1.add(Arrays.asList("-","-","-","-","-","-","-","-"));
    list1.add(Arrays.asList("-","-","-","-","-","-","-","-"));
    list1.add(Arrays.asList("-","-","-","-","-","-","-","-"));
    list1.add(Arrays.asList("P1","P1","P1","P1","P1","P1","P1","P1"));
    list1.add(Arrays.asList("R1","N1","B1","Q1","K1","B1","N1","R1"));

    list2.add(Arrays.asList("-","C2","-","C2","-","C2","-","C2"));
    list2.add(Arrays.asList("C2","-","C2","-","C2","-","C2","-"));
    list2.add(Arrays.asList("-","C2","-","C2","-","C2","-","C2"));
    list2.add(Arrays.asList("-","-","-","-","-","-","-","-"));
    list2.add(Arrays.asList("-","-","-","-","-","-","-","-"));
    list2.add(Arrays.asList("C1","-","C1","-","C1","-","C1","-"));
    list2.add(Arrays.asList("-","C1","-","C1","-","C1","-","C1"));
    list2.add(Arrays.asList("C1","-","C1","-","C1","-","C1","-"));

    list3.add(Arrays.asList("-","R2","N2","B2","Q2","S2","B2","N2","R2","-"));
    list3.add(Arrays.asList("-","-","-","P2","P2","P2","P2","-","-","-"));
    list3.add(Arrays.asList("-","-","-","P2","P2","P2","-","-","-","-"));
    list3.add(Arrays.asList("-","-","-","-","P2","-","-","-","-","-"));
    list3.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
    list3.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
    list3.add(Arrays.asList("-","-","-","-","-","P1","-","-","-","-"));
    list3.add(Arrays.asList("-","-","-","-","P1","P1","P1","-","-","-"));
    list3.add(Arrays.asList("-","-","-","P1","P1","P1","P1","-","-","-"));
    list3.add(Arrays.asList("-","R1","N1","B1","Q1","S1","B1","N1","R1","-"));

    board.initialize(parser.getInitialStates(), "ooga/model/resources/chess.properties");
    board2.initialize(parser2.getInitialStates(),"ooga/model/resources/chess.properties");
    cBoard.initialize(parser3.getInitialStates(), "ooga/model/resources/checkers.properties");
    checkBoard1.initialize(parserCheck1.getInitialStates(), "ooga/model/resources/chess.properties");
    checkBoard2.initialize(parserCheck2.getInitialStates(), "ooga/model/resources/chess.properties");
    pawnBoard1.initialize(parserPawn1.getInitialStates(), "ooga/model/resources/chess.properties");
    pawnBoard2.initialize(parserPawn2.getInitialStates(), "ooga/model/resources/chess.properties");
    castleBoard1.initialize(parserCastle1.getInitialStates(), "ooga/model/resources/chess.properties");
    castleBoard2.initialize(parserCastle2.getInitialStates(), "ooga/model/resources/chess.properties");
    castleRight1.initialize(parserCastleRight1.getInitialStates(), "ooga/model/resources/chess.properties");
    castleRight2.initialize(parserCastleRight2.getInitialStates(), "ooga/model/resources/chess.properties");
    castleLeft1.initialize(parserCastleLeft1.getInitialStates(), "ooga/model/resources/chess.properties");
    castleLeft2.initialize(parserCastleLeft2.getInitialStates(), "ooga/model/resources/chess.properties");

  }
  @Test
  void initialize() {
    testVals(list1,board);
    testVals(list3,board2);
    testVals(list2,cBoard);
  }

  void testVals(List<List<String>> expected, Board b){
    for(int i=7; i<b.getBoardLength(); i++){
      for(int j=0; j<b.getBoardWidth(); j++){
        if(expected.get(i).get(j)=="-"){
          assertNull(b.getPieceAt(i,j));
        }
        else{
          assertEquals(expected.get(i).get(j).substring(0,1), String.valueOf(b.getPieceAt(i,j).getKey()));
          assertEquals(expected.get(i).get(j).substring(1), String.valueOf(b.getPieceAt(i,j).getTeam()));
        }

      }
    }
  }


  @Test
  void clearGame() {
    board.clearGame();
    assertEquals(0, board.getBoardLength());
    assertEquals(0, board.getValidMoves().size());
    assertNull(board.getActiveTeam());
    assertNull(board.getCurrentPiece());
    board2.clearGame();
    assertEquals(0, board2.getBoardLength());
    assertEquals(0, board2.getValidMoves().size());
    assertNull(board2.getActiveTeam());
    assertNull(board2.getCurrentPiece());
    cBoard.clearGame();
    assertEquals(0, cBoard.getBoardLength());
    assertEquals(0, cBoard.getValidMoves().size());
    assertNull(cBoard.getActiveTeam());
    assertNull(cBoard.getCurrentPiece());

  }

  @Test
  void setAndGetActiveTeam() {
    int t =(board.getActiveTeam().getTeamNum()==1)?2:1;
    board.setActiveTeam();
    assertEquals(t, board.getActiveTeam().getTeamNum());
    int t0 =(board.getActiveTeam().getTeamNum()==1)?2:1;
    board.setActiveTeam();
    assertEquals(t0, board.getActiveTeam().getTeamNum());
    int t1 =(board2.getActiveTeam().getTeamNum()==1)?2:1;
    board2.setActiveTeam();
    assertEquals(t1, board2.getActiveTeam().getTeamNum());
    int t2 =(cBoard.getActiveTeam().getTeamNum()==1)?2:1;
    cBoard.setActiveTeam();
    assertEquals(t2, cBoard.getActiveTeam().getTeamNum());
  }


  @Test
  void updatePieces() {
    cBoard.setCurrentPiece(cBoard.getPieceAt(2,1));
    cBoard.setCurrentRow(2);
    cBoard.setCurrentCol(1);
    cBoard.movePiece(3,0);
    assertNull(cBoard.getPieceAt(2,1));
    assertNotNull(cBoard.getPieceAt(3,0));

    board.setCurrentPiece(board.getPieceAt(1,0));
    board.setCurrentRow(1);
    board.setCurrentCol(0);
    board.movePiece(2,0);
    assertNull(cBoard.getPieceAt(2,0));
    assertNotNull(cBoard.getPieceAt(1,0));
  }

  @Test
  void setAndGetCurrentRow() {
    board.setCurrentRow(1);
    assertEquals(1, board.getCurrentRow());
    board2.setCurrentRow(1);
    assertEquals(1, board2.getCurrentRow());
  }

  @Test
  void setAndGetCurrentCol() {
    board.setCurrentCol(1);
    assertEquals(1, board.getCurrentCol());
    board2.setCurrentCol(1);
    assertEquals(1, board2.getCurrentCol());
  }

  @Test
  void setAndGetCurrentPiece() {
    board.setCurrentPiece(board.getPieceAt(0,0));
    assertEquals('R', board.getCurrentPiece().getKey());
    assertEquals(2, board.getCurrentPiece().getTeam());
    cBoard.setCurrentPiece(cBoard.getPieceAt(0,1));
    assertEquals('C', cBoard.getCurrentPiece().getKey());
    assertEquals(2, board.getCurrentPiece().getTeam());
    assertNull(board.getPieceAt(2,0));
    assertNull(cBoard.getPieceAt(0,0));
  }


  @Test
  void getTeams() {
    assertEquals(2,cBoard.getTeams().size());
    assertEquals(2, board.getTeams().size());
    assertEquals(2, board2.getTeams().size());
  }




  @Test
  void validateGeneralMove() {
    assertTrue(cBoard.validateGeneralMove(3,0));
    assertFalse(cBoard.validateGeneralMove(-1,1));
    assertFalse(cBoard.validateGeneralMove(100,10));
  }

  @Test
  void validateNewPosition() {
  }

  @Test
  void addAndGetValidMove() {
    cBoard.findValidMoves(2,1);
    assertEquals(2,cBoard.getValidMoves().size());
    board.findValidMoves(1,1);
    assertEquals(2,board.getValidMoves().size());

  }

  @Test
  void getBoardLength() {
    assertEquals(8,cBoard.getBoardLength());
    assertEquals(8,board.getBoardLength());
    assertEquals(10,board2.getBoardLength());
  }

  @Test
  void clearPossibleMoves() {
    cBoard.findValidMoves(2,1);
    cBoard.clearPossibleMoves();
    assertEquals(0,cBoard.getValidMoves().size());
    board.findValidMoves(1,1);
    board.clearPossibleMoves();
    assertEquals(0,board.getValidMoves().size());
  }

  @Test
  void getTeam() {
    assertEquals(1,cBoard.getTeam(1).getTeamNum());
    assertEquals(1,board.getTeam(1).getTeamNum());
    assertEquals(2,board2.getTeam(2).getTeamNum());
    assertNull(cBoard.getTeam(0));
  }

  @Test
  void getBoardWidth() {
    assertEquals(8,cBoard.getBoardWidth());
    assertEquals(8,board.getBoardWidth());
    assertEquals(10,board2.getBoardWidth());
  }

  @Test
  void validateBounds() {
  }

  @Test
  void checkForCheck(){
    if(checkBoard1.getActiveTeam().getTeamNum()!=1){
      checkBoard1.setActiveTeam();
    }
    if(checkBoard2.getActiveTeam().getTeamNum()!=1){
      checkBoard2.setActiveTeam();
    }
    assertTrue(((ChessBoard)checkBoard1).checkForCheck());
    assertFalse(((ChessBoard)board).checkForCheck());
    assertTrue(((ChessBoard)checkBoard2).checkForCheck());
  }

  @Test
  void checkPawnPromotion(){
    assertFalse(board.checkPawnPromotion());
    assertTrue(pawnBoard1.checkPawnPromotion());
    assertTrue(pawnBoard2.checkPawnPromotion());
  }

  @Test
  void canCastleRightRook(){
    assertTrue(((ChessBoard)castleBoard1).canCastleRightRook());
    assertTrue(((ChessBoard)castleBoard2).canCastleRightRook());
    assertTrue(((ChessBoard)castleRight1).canCastleRightRook());
    assertTrue(((ChessBoard)castleRight2).canCastleRightRook());
  }

  @Test
  void canCastleLeftRook(){
    assertTrue(((ChessBoard)castleBoard1).canCastleLeftRook());
    assertTrue(((ChessBoard)castleBoard2).canCastleLeftRook());
    assertTrue(((ChessBoard)castleLeft1).canCastleLeftRook());
    assertTrue(((ChessBoard)castleLeft2).canCastleLeftRook());
  }

}

