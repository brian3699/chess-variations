package ooga.model.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.model.piece.PieceInterface;

public class Team implements TeamInterface {

  private int myTeamNum;
  private int myScore;
  private List<PieceInterface> myActivePieces;
  private Map<Integer, List<PieceInterface>> myCaptured;
  private int myTeamSize;

  /**
   * creates a new team with
   *
   * @param teamNum is the team number associated with the new team
   */
  public Team(int teamNum) {
    this.myTeamNum = teamNum;
    this.myScore = 0;
    myActivePieces = new ArrayList<>();
    myCaptured = new HashMap<>();
    myTeamSize = 0;
  }

  /**
   * adds a piece to the list of active pieces for this team
   *
   * @param p is the piece that is now active on this team
   */
  public void setActivePiece(PieceInterface p) {
    myActivePieces.add(p);
    myTeamSize += 1;
  }

  /**
   * gets this team's score
   *
   * @return myScore
   */
  public int getScore() {
    return myScore;
  }

  /**
   * increments myScore some number of points
   *
   * @param points holds the number of points to be added to score
   */
  private void setScore(int points) {
    myScore += points;
    System.out.print(myTeamNum + ":" + myScore);
  }

  /**
   * gets list of this team's active pieces
   *
   * @return myActivePieces
   */
  public List<PieceInterface> getActive() {
    return myActivePieces;
  }

  /**
   * gets map of pieces that this team has captured
   *
   * @return myCaptured
   */
  public Map<Integer, List<PieceInterface>> getCaptured() {
    return myCaptured;
  }

  /**
   * gets this team's team number
   *
   * @return myTeamNum
   */
  public int getTeamNum() {
    return myTeamNum;
  }

  /**
   * adds a new piece to the map of captured pieces for this team
   *
   * @param team  is the team number of the piece that was
   * @param piece is the piece that was just captured
   */
  public void setCaptured(int team, PieceInterface piece) {
    myCaptured.putIfAbsent(team, new ArrayList());
    myCaptured.get(team).add(piece);
    setScore(piece.getPoints());
  }

  /**
   * get the size of the team
   *
   * @return int of team size
   */
  public int getSizeOfTeam() {
    return myTeamSize;
  }

  public void removeActive(PieceInterface p) {
    myActivePieces.remove(p);
    myTeamSize -= 1;
  }
}
