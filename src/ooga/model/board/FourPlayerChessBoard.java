//package ooga.model.board;
//
//public class FourPlayerChessBoard extends ChessBoard {
//
//  private static final int SINGLE_CELL = 1;
//  private static final int ZERO_CELL = 0;
//  private static final int HALF = 2;
//  private int minCol;
//  private int minRow;
//  private int maxCol;
//  private int maxRow;
//
//  public FourPlayerChessBoard() {
//    super();
//  }
//
//  private void setBoundaryVals() {
//    for(int i=0; i<getBoardLength()/HALF; i++)
//    {
//      for(int j=0; j<getBoardWidth()-1;j++){
//        if(getPieceAt(i,j)==null && getPieceAt(i, j+SINGLE_CELL)!=null){
//          minCol = j;
//          minRow = i+SINGLE_CELL;
//        }
//        if(getPieceAt(i,j+SINGLE_CELL)==null && getPieceAt(i, j)!=null){
//          maxCol = j;
//        }
//      }
//    }
//    for(int i=getBoardLength()/HALF; i<getBoardLength(); i++){
//      for(int j=0; j<getBoardWidth();j++){
//        if(getPieceAt(i,j)==null && getPieceAt(i, j+SINGLE_CELL)!=null){
//          maxRow = i-SINGLE_CELL;
//          return;
//        }
//      }
//    }
//  }
//
//  protected boolean validateBounds(int row, int col){
//    setBoundaryVals();
//    return row < ZERO_CELL || row >= getBoardLength() || col < ZERO_CELL || col >= getBoardWidth()||(row<minRow && col<minCol) ||(row<minRow && col>maxCol)||(row>maxRow && col<minCol)||(row>maxRow && col>maxCol);
//  }
//}
