package ooga.model.board;

import com.opencsv.exceptions.CsvValidationException;
import ooga.model.piece.Piece;
import ooga.model.piece.PieceInterface;
import ooga.util.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.ReflectionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameAreaEditorBoardTest {
    private static final String CSV_PATH = "data/boards/";
    private static final String PROPERTY_PATH = "ooga/model/resources/";
    private static final String PROPERTIES = ".properties";
    private static final String className = "GameAreaEditor";

    public List<List<String>> list1= new ArrayList<List<String>>();
    private GameAreaEditorBoard myBoard;

    @BeforeEach
    void setUp() throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException {
        myBoard = new GameAreaEditorBoard();
        CSVParser myParser = new CSVParser();
        File file = new File (CSV_PATH + "Custom10x10" + "_board.csv");
        myParser.readCSVFile(file);
        myBoard.initialize(myParser.getInitialStates(), PROPERTY_PATH + className.toLowerCase() + PROPERTIES);

        list1.add(Arrays.asList("P1","-","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","K2","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
        list1.add(Arrays.asList("-","-","-","-","-","-","-","-","-","-"));
    }

    @Test
    void testInitializePotentialPiecesGood() throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException {
        CSVParser myParser = new CSVParser();
        File file = new File (CSV_PATH + "PotentialPieces" + "_board.csv");
        myParser.readCSVFile(file);
        myBoard.initializePotentialPieces(myParser.getInitialStates(), PROPERTY_PATH + className.toLowerCase() + PROPERTIES);


        testVals(list1,myBoard);
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
    void findValidMovesTest() throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException {
        CSVParser myParser = new CSVParser();
        File file = new File (CSV_PATH + "PotentialPieces" + "_board.csv");
        myParser.readCSVFile(file);
        myBoard.initializePotentialPieces(myParser.getInitialStates(), PROPERTY_PATH + className.toLowerCase() + PROPERTIES);

        myBoard.findValidMoves(3,0);
        assertEquals(0, myBoard.getValidMoves().get(0).deltaRow());
        assertEquals(1, myBoard.getValidMoves().get(0).deltaCol());
        assertEquals(10*10-2, myBoard.getValidMoves().size());

    }


    @Test
    void movePieceTest() throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException {
        CSVParser myParser = new CSVParser();
        File file = new File (CSV_PATH + "PotentialPieces" + "_board.csv");
        myParser.readCSVFile(file);
        myBoard.initializePotentialPieces(myParser.getInitialStates(), PROPERTY_PATH + className.toLowerCase() + PROPERTIES);

        PieceInterface piece = myBoard.getPieceAtSelect(0,1);
        myBoard.setCurrentPiece(piece);
        myBoard.movePiece(3,2);

        assertEquals(80, myBoard.getPieceAt(3,2).getKey());
    }

    @Test
    void findFullSpacesTest() throws CsvValidationException, IOException, ReflectionException, ClassNotFoundException, NoSuchMethodException {
        CSVParser myParser = new CSVParser();
        File file = new File (CSV_PATH + "PotentialPieces" + "_board.csv");
        myParser.readCSVFile(file);
        myBoard.initializePotentialPieces(myParser.getInitialStates(), PROPERTY_PATH + className.toLowerCase() + PROPERTIES);

        myBoard.findFullSpaces();
        assertEquals(0, myBoard.getValidMoves().get(0).deltaRow());
        assertEquals(0, myBoard.getValidMoves().get(0).deltaCol());
        assertEquals(2, myBoard.getValidMoves().size());
    }

}

