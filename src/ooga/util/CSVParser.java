package ooga.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ooga.model.util.Grid;

public class CSVParser implements CSVParserInterface {

  private int[] gridDimensions;
  private Grid initialStates;

  /**
   * Reads in the CSV file and parses the data to fit the Grid implementation
   *
   * @param file CSV file to be read
   * @throws IOException
   * @throws CsvValidationException
   */
  @Override
  public void readCSVFile(File file)
      throws IOException, CsvValidationException, IllegalArgumentException, NumberFormatException {
    FileReader filereader = new FileReader(file.getPath());
    CSVReader csvReader = new CSVReader(filereader);
    setDimensions(csvReader.readNext());
    writeInitialStates(csvReader);
  }

  /**
   * Takes the rows and columns inputted from the file and initializes those values
   */
  private void setDimensions(String[] dimensions) throws NumberFormatException {
    gridDimensions = new int[]{Integer.parseInt(dimensions[1]), Integer.parseInt(dimensions[0])};

  }


  /**
   * Takes each element in the CSV file and puts it into a 2D grid Package Protected
   *
   * @param csvReader
   * @throws IOException
   * @throws CsvValidationException
   * @throws IllegalArgumentException
   */
  void writeInitialStates(CSVReader csvReader)
      throws IOException, CsvValidationException, IllegalArgumentException {
    String[][] intermediateStates = new String[gridDimensions[0]][gridDimensions[1]];
    for (int i = 0; i < gridDimensions[0]; i++) {
      String[] nextRecord = csvReader.readNext();
      for (int j = 0; j < gridDimensions[1]; j++) {
        intermediateStates[i][j] = validateEntry(nextRecord[j]);
      }
    }
    initialStates = new Grid(intermediateStates);
  }

  /**
   * Validates that a given entry follows the format of 1 char and 1 digit. Char = the character
   * representing the piece type Note: the 'else' statements are technically redundant but are shown
   * to make the code more readable.
   *
   * @param s input string, to be returned if the entry is valid
   * @return original input if the entry is valid
   * @throws IllegalArgumentException throws exception if the string is not a single character
   */
  String validateEntry(String s) throws IllegalArgumentException {
    if (s.length() == 2 && Character.isLetter(s.charAt(0)) && Character.isDigit(s.charAt(1))) {
      return s;
    } else if (s.equals("-")) {
      return "";
    } else { //else is technically redundant here, but is provided to make the logic easier to follow
      throw new IllegalArgumentException(
          "Improper Entry in CSV file. Must be format: \"K1\",\"P2\",etc.");
    }
  }

  /**
   * Gets the rows and columns for the created Grid
   *
   * @return the rows and columns for the Grid
   */
  public int[] getDimensions() {
    return gridDimensions;
  }

  /**
   * Gets the initial states of the Grid
   *
   * @return the initial states of the Grid
   */
  public Grid getInitialStates() {
    return initialStates;
  }

  /**
   * Gets the current game and saves it to a csv file
   *
   * @param grid passes in the current state of the game
   */
  @Override
  public void saveGame(String path, Grid grid) throws IOException {
    path = path + ".csv"; // add extension
    FileWriter fileWriter = new FileWriter(path);
    CSVWriter csvFile =
        new CSVWriter(
            fileWriter,
            ',',
            ICSVWriter.NO_QUOTE_CHARACTER,
            ICSVWriter.NO_ESCAPE_CHARACTER,
            ICSVWriter.DEFAULT_LINE_END);

    // write number of rows and columns
    String[] firstLine = {String.valueOf(grid.width()), String.valueOf(grid.length())};
    csvFile.writeNext(firstLine);
    for (int i = 0; i < grid.length(); i++) {
      String[] currentRow = new String[grid.width()];
      for (int j = 0; j < grid.width(); j++) {
        currentRow[j] = grid.getVal(i, j);
      }
      csvFile.writeNext(currentRow);
    }

    csvFile.close();
    fileWriter.close();

  }
}