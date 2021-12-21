package ooga.model.team;

import java.util.List;
import java.util.Map;
import ooga.model.piece.PieceInterface;

public interface TeamInterface {

  void setActivePiece(PieceInterface p);

  int getScore();

  List<PieceInterface> getActive();

  Map<Integer, List<PieceInterface>> getCaptured();

  int getTeamNum();

  void setCaptured(int team, PieceInterface piece);

  int getSizeOfTeam();

  void removeActive(PieceInterface piece);
}
