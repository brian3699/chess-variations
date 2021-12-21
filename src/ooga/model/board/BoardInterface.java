package ooga.model.board;

import java.util.ArrayList;
import java.util.List;
import javax.management.ReflectionException;

import ooga.model.piece.Piece;
import ooga.model.piece.PieceInterface;
import ooga.model.team.TeamInterface;
import ooga.model.util.Grid;
import ooga.model.util.Move;

public interface BoardInterface {

  void initialize(Grid parsedBoard, String piecePath)
      throws ReflectionException, ClassNotFoundException, NoSuchMethodException;

  void findValidMoves(int x, int y);

  void movePiece(int x, int y);

  void clearGame();

  int getHasWon();

  List<Move> getValidMoves();

  PieceInterface getPieceAt(int x, int y);

  TeamInterface getActiveTeam();

  int getCurrentRow();

  int getCurrentCol();

  void setActiveTeam();

  boolean checkPawnPromotion();

  void promotePawn(char id);

  ArrayList<String> getCapturedPieces();

  PieceInterface getPawnPromotionPiece();

  int getPieceRow(PieceInterface piece);

  int getPieceCol(PieceInterface piece);
}
