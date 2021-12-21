package ooga.util;

import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.IOException;
import ooga.model.util.Grid;

public interface CSVParserInterface {

  public void readCSVFile(File file)
      throws IOException, CsvValidationException, IllegalArgumentException;

  public int[] getDimensions();

  public Grid getInitialStates();

  public void saveGame(String path, Grid grid) throws IOException;

}
