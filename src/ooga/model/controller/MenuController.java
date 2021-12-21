package ooga.model.controller;

import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.management.ReflectionException;

import ooga.view.components.UserInputBuilder;
import ooga.view.game.GameMenuView;
import ooga.view.game.LanguageChoiceView;

import java.io.IOException;

public class MenuController {

  private static final String DEFAULT_LANGUAGE_FILE = "ooga.view.resources.language.English";
  private static final String MODEL_PATH = "ooga.model.controller.";
  private static final String CONTROLLER = "Controller";
  private static final CharSequence CHESS = "Chess";
  private static final String CSV_PATH = "data/boards/";
  private static final String START_GAME = "StartNewGame";
  private static final String CHECKERS = "Checkers";
  private static final String CHRISTMAS = "ChristmasChess";
  private static final String BIG_BOARD = "BigBoardChess";
  private static final String FOUR_PLAYER = "FourPlayerChess";
  private static final String CUSTOM = "GameAreaEditor";
  private static final String PLAY_CHESS = "PlayChess";
  private static final String PLAY_CHECKERS = "PlayCheckers";
  private static final String PLAY_CHRISTMAS = "PlayChristmasChess";
  private static final String PLAY_BIG_BOARD = "PlayBigBoardChess";
  private static final String PLAY_FOUR = "PlayFourPlayerChess";
  private static final String PLAY_CUSTOM = "PlayCustomChess";
  private static final String LAUNCH_EDITOR = "LaunchEditor";
  private static final String RETURN_HOME = "ReturntoHome";
  private static final String UPLOAD_SAVE = "UploadSavedGame";
  private static final String UPLOAD_Custom = "UploadCustom";
  private static final String BOARD_CSV = "_board.csv";
  private static final String CHOOSE_COLOR_SCHEME = "ChooseColorScheme";
  private static final int STAGE_WIDTH = 800;
  private static final int STAGE_HEIGHT = 200;
  public static final String CUSTOM_BOARD = "Custom10x10_board.csv";

  private Map<String, EventHandler> buttonMap;
  private Stage myStage;
  private LanguageChoiceView languageChoiceView;
  private GameMenuView menuView; //TODO: Change GameOptionsView to View after updating API
  private ResourceBundle languageResource;
  private UserInputBuilder userInputBuilder;
  private List<Color> colorChoice;
  private Controller controller;


  /**
   * MainController instantiates GameOptionView and is responsible for communication between backend
   * and frontend about the options of the game the user(s) chooses
   *
   * @param stage stage
   */
  public MenuController(Stage stage) {
    myStage = stage;
    myStage.setHeight(STAGE_HEIGHT);
    myStage.setWidth(STAGE_WIDTH);
    populateButtonMap();
    languageResource = ResourceBundle.getBundle(DEFAULT_LANGUAGE_FILE);
    languageChoiceView = new LanguageChoiceView();
    colorChoice = new ArrayList<>();
    menuView = new GameMenuView(ResourceBundle.getBundle(DEFAULT_LANGUAGE_FILE));
    userInputBuilder = new UserInputBuilder(ResourceBundle.getBundle(DEFAULT_LANGUAGE_FILE));
  }

  public void startLanguageChoiceView() {
    languageResource = languageChoiceView.generateLanguageOptions();
    menuView = new GameMenuView(languageResource);
    userInputBuilder = new UserInputBuilder(languageResource);
  }

  private void populateButtonMap() {
    buttonMap = new TreeMap();
    // TODO: Refactor these strings so they aren't hard coded
    buttonMap.put(PLAY_CHESS, event -> newOrSavedGame(
        CHESS.toString())); // TODO: Needs to use reflection later when we have more methods
    buttonMap.put(PLAY_CHECKERS, event -> newOrSavedGame(CHECKERS));
    buttonMap.put(PLAY_CHRISTMAS, event -> newOrSavedGame(CHRISTMAS));
    buttonMap.put(PLAY_BIG_BOARD, event -> newOrSavedGame(BIG_BOARD));
    buttonMap.put(PLAY_FOUR, event -> newOrSavedGame(FOUR_PLAYER));
    buttonMap.put(PLAY_CUSTOM, event -> setupCustomGame(CUSTOM));
  }

  private void setupCustomGame(String gameType) {
    Map<String, EventHandler> buttonMap = new LinkedHashMap<>();
    buttonMap.put(RETURN_HOME, event -> startOptionsView());
    buttonMap.put(UPLOAD_Custom, event -> {
      try {
        File file = uploadGame();
        startGame(gameType, file);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    File file = new File(CSV_PATH + CUSTOM_BOARD);
    buttonMap.put(CHOOSE_COLOR_SCHEME, event -> getUserColorChoice());
    buttonMap.put(LAUNCH_EDITOR, event -> {
      try {
        startGame(gameType, file);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    myStage.setScene(menuView.generateMenuScene(buttonMap));
    myStage.show();
  }

  private void newOrSavedGame(String gameType) {
    Map<String, EventHandler> buttonMap = new LinkedHashMap<>();
    buttonMap.put(RETURN_HOME, event -> startOptionsView());
    buttonMap.put(UPLOAD_SAVE, event -> {
      try {
        File file = uploadGame();
        startGame(gameType, file);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    buttonMap.put(CHOOSE_COLOR_SCHEME, event -> getUserColorChoice());
    buttonMap.put(START_GAME, event -> {
      try {
        startGame(gameType, null); // Default Game
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    myStage.setScene(menuView.generateMenuScene(buttonMap));
    myStage.show();

  }

  private void getUserColorChoice() {
    colorChoice = userInputBuilder.getUserColorChoice();
  }

  /**
   * instantiates the GameOptionsView and shows it
   */
  public void startOptionsView() {
    myStage.setScene(menuView.generateMenuScene(buttonMap));
    myStage.show();
  }

  private File uploadGame() {
    FileChooser file = new FileChooser();
    File chosenFile = file.showOpenDialog(myStage);
    return chosenFile;
  }

  /**
   * this method starts the game controller based on menu button selection
   *
   * @param gameType   the type of game the player chose
   * @param passedFile the csv file for the grid set up of the file
   * @throws ReflectionException throws exception if gametype class isn't found
   */
  public void startGame(String gameType, File passedFile)
      throws ReflectionException { // TODO : error handling
    String fileName =
        (passedFile == null) ? CSV_PATH + gameType + BOARD_CSV : passedFile.getAbsolutePath();
    File file = new File(fileName);
    controller = createController(e -> startOptionsView(), gameType, file);
    startOptionsView();
    controller.startGame(new Stage());
  }

  protected Controller createController(EventHandler event, String gameType, File file)
      throws ReflectionException {
    String gameT =
        (gameType.toLowerCase().contains(CHESS.toString().toLowerCase())) ? CHESS.toString()
            : gameType;
    try {
      Class<?> game = Class.forName(MODEL_PATH + gameT + CONTROLLER);
      Constructor constructor = game.getDeclaredConstructor(EventHandler.class, String.class,
          ResourceBundle.class, File.class);
      Controller component = (Controller) constructor.newInstance(event, gameType, languageResource,
          file);
      return component;

    } catch (RuntimeException
        | ClassNotFoundException
        | NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      e.printStackTrace();
      throw new ReflectionException(e);
    }
  }

  protected Controller getController() {
    return controller;
  }
}
