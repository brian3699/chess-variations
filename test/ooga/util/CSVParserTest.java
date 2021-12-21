package ooga.util;

import ooga.util.CSVParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CSVParserTest {

    private static final String DEFAULT_RESOURCE_PACKAGE = "data/boards/";
    private static final String INVALID_RESOURCE_PACKAGE = DEFAULT_RESOURCE_PACKAGE + "invalid/";

    @Test
    void invalidFile() {
        CSVParser csvParser = new CSVParser();
        File file = new File("invalidfilename");
        assertThrows(FileNotFoundException.class, () -> csvParser.readCSVFile(file));
    }

    @Test
    /**
     * e.g. s,g
     */
    void invalidDimensionsChar() {
        CSVParser csvParser = new CSVParser();
        File file = new File(INVALID_RESOURCE_PACKAGE + "invalidCharDimensions_board.csv");
        assertThrows(NumberFormatException.class, () -> csvParser.readCSVFile(file));
    }

    @Test
    /**
     * e.g. sdf,lak
     */
    void invalidDimensionsString() {
        CSVParser csvParser = new CSVParser();
        File file = new File(INVALID_RESOURCE_PACKAGE + "invalidString_board.csv");
        assertThrows(NumberFormatException.class, () -> csvParser.readCSVFile(file));
    }

    @Test
    /**
     * Should still work.
     * e.g. "4afad"
     */
    void invalidDimensionsIntThenString() {
        CSVParser csvParser = new CSVParser();
        File file = new File(INVALID_RESOURCE_PACKAGE + "invalidIntThenString_board.csv");
        assertThrows(NumberFormatException.class, () -> csvParser.readCSVFile(file));
    }

    @Test
    /**
     * Should still work.
     * e.g. "5a8"
     */
    void multipleInts() {
        CSVParser csvParser = new CSVParser();
        File file = new File(INVALID_RESOURCE_PACKAGE + "multipleInts_board.csv");
        assertThrows(NumberFormatException.class, () -> csvParser.readCSVFile(file));
    }

    @Test
    /**
     * The indicated number of dimensions differs from the actual dimensions provided
     */
    void givenDimensionsSmallerThanActual() {
        CSVParser csvParser = new CSVParser();
        File file = new File(INVALID_RESOURCE_PACKAGE + "invalidDifferentDimensions.csv");
        assertThrows(NumberFormatException.class, () -> csvParser.readCSVFile(file));
    }

    @Test
    /**
     * The indicated number of dimensions differs from the actual dimensions provided
     */
    void givenDimensionsLargerThanActual() {
        CSVParser csvParser = new CSVParser();
        File file = new File(INVALID_RESOURCE_PACKAGE + "invalidDifferentDimensions2.csv");
        assertThrows(NumberFormatException.class, () -> csvParser.readCSVFile(file));
    }

    @Test
    /**
     * The length of an item is longer than 2.
     */
    void charItemLengthOver2() {
        CSVParser csvParser = new CSVParser();
        assertThrows(IllegalArgumentException.class, () -> csvParser.validateEntry("K22"));
    }

    @Test
    /**
     * The length of an item is longer than 1 (e.i. two commaas in a row)
     */
    void charOfLengthZero() {
        CSVParser csvParser = new CSVParser();
        assertThrows(IllegalArgumentException.class, () -> csvParser.validateEntry(""));
    }
}
