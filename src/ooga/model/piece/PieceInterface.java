package ooga.model.piece;

import java.util.List;
import ooga.model.util.Direction;
import ooga.model.util.Layout;
import ooga.model.util.Move;
import ooga.model.util.Stream;

public interface PieceInterface {

  char getKey();

  int getPoints();

  void setPoints(int points);

  List<Move> getPossibleMoves();

  void setPossibleMoves(List<Move> moves);

  int getTeam();

  void setTeam(int team);

  Direction findDirection();

  void setPieceProperties(Stream data);

  int getMoveIterations();

  void setMoveIterations(int i);

  boolean isValidMove(int moveRow, int moveCol, Layout currentLayout);
}
