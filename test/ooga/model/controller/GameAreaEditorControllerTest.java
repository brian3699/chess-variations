package ooga.model.controller;

import javafx.event.EventHandler;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.ReflectionException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GameAreaEditorControllerTest {
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
    private static final String PLAY_CHESS ="PlayChess";
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
    private static final String DEFAULT_LANGUAGE_DIRECTORY = "ooga.view.resources.language.";
    private static final String LANGUAGE_LIST_DIRECTORY = "ooga.view.resources.language.LanguageList";
    private static final String LANGUAGE_OPTION_TITLE = "Please Select a Language ";
    private static final String DEFAULT_LANGUAGE = "English";

    private ResourceBundle languageResource;


    GameAreaEditorController myController;
    @BeforeEach
    void setUp() throws ReflectionException {
        //"English"
        languageResource = ResourceBundle.getBundle(DEFAULT_LANGUAGE_DIRECTORY + DEFAULT_LANGUAGE);
        Map<String, EventHandler> buttonMap = new LinkedHashMap<>();
        File file = new File (CSV_PATH + "Custom10x10" + "_board.csv");
        myController = (GameAreaEditorController) createController(e -> doNothing(), "GameAreaEditor", file);
    }

//    /**
//     * Create choice dialog box to choose the language of the game and return ResourceBundle of the selected language
//     * @return ResourceBundle of the selected language
//     */
//    public ResourceBundle generateLanguageOptions() {
//        ChoiceDialog<String> langDialog = userInputBuilder.makeChoiceDialog(DEFAULT_LANGUAGE, languageOptions, LANGUAGE_OPTION_TITLE);
//        langDialog.showAndWait();
//        languageResource = makeResourceBundle(langDialog.getSelectedItem());
//        return languageResource;
//    }
//
//    private ResourceBundle makeResourceBundle(String lang) {
//        try {
//            return ResourceBundle.getBundle(DEFAULT_LANGUAGE_DIRECTORY + lang);
//        } catch (Exception e) {
//            return ResourceBundle.getBundle(DEFAULT_LANGUAGE_DIRECTORY + DEFAULT_LANGUAGE);
//        }
//    }

    private Controller createController(EventHandler event, String gameType, File file)
            throws ReflectionException {
        //TODO: simplify this logic?
        String gameT= (gameType.toLowerCase().contains(CHESS.toString().toLowerCase()))?CHESS.toString():gameType;
        try {
            Class<?> game = Class.forName(MODEL_PATH + gameT + CONTROLLER);
            Constructor constructor = game.getDeclaredConstructor(EventHandler.class, String.class, ResourceBundle.class, File.class);
            Controller component = (Controller) constructor.newInstance(event, gameType, languageResource, file);
            return component;

        }
        // TODO: package exceptions
        catch (RuntimeException
                | ClassNotFoundException
                | NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
            throw new ReflectionException(e);
        }
    }


    private void doNothing() {
    }

    @Test
    void getGameButtons() {
    }

    @Test
    void getGameClass() {
    }

    @Test
    void onCellClick() {
    }

    @Test
    void makeMove() {
    }

}
