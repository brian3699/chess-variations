package ooga.model.controller;

import java.io.File;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.management.ReflectionException;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class CustomChessControllerTest extends DukeApplicationTest {
  private Controller c;
  Stage pStage;
  MenuController m;
  File f;

  @Override
  public void start(Stage PrimaryStage) throws ReflectionException {
    pStage =new Stage();
    m= new MenuController(pStage);
    m.startOptionsView();
    pStage.hide();
  }

  @Test
  void customBoardCreate(){
    Button chessCustom = lookup("PlayCustomChess").query();
    runAsJFXAction(()->clickOn(chessCustom));
  }

}
