package ooga.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameParserTest {
    private static final String DEFAULT_RESOURCE_PACKAGE = "data/variations/";
    private static final String INVALID_RESOURCE_PACKAGE = DEFAULT_RESOURCE_PACKAGE + "invalid/";

    @Test
    void invalidFile() {
        GameParser gameParser = new GameParser();
        File file = new File("invalidfilename");
        assertThrows(FileNotFoundException.class, () -> gameParser.readSimFile(file));
    }


}
