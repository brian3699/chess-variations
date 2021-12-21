//package ooga.model.board;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.opencsv.exceptions.CsvValidationException;
//import java.io.File;
//import java.io.IOException;
//import javax.management.ReflectionException;
//import ooga.model.util.Move;
//import ooga.util.CSVParser;
//import ooga.util.CSVParserInterface;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//class FourPlayerChessBoardTest {
//  Board board;
//  CSVParserInterface parser;
//
//  @BeforeEach
//  void setUp() throws ReflectionException, CsvValidationException, IOException {
//    CSVParserInterface parser = new CSVParser();
//    board = new FourPlayerChessBoard();
//    parser.readCSVFile(new File("data/boards/FourPlayerChess_board.csv"));
//    board.initialize(parser.getInitialStates(), "ooga/model/resources/chess.properties");
//}
//  @Test
//  void validateBounds() {
//    board.findValidMoves(0,3);
//    for(Move m: board.getValidMoves()){
//      assertFalse(m.deltaCol()<=2);
//    }
//    board.findValidMoves(0,10);
//    for(Move m: board.getValidMoves()){
//      assertFalse(m.deltaCol()>=11);
//    }
//    board.findValidMoves(3,0);
//    for(Move m: board.getValidMoves()){
//      assertFalse(m.deltaRow()<=2);
//    }
//    board.findValidMoves(10,0);
//    for(Move m: board.getValidMoves()){
//      assertFalse(m.deltaRow()>=11);
//    }
//  }
//
//}