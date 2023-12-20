import board.Board;
import piece.Move;

import java.util.List;

public class Chess {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        board.loadFen(Board.baseFen, true);
        List<Move> moves = board.pieces.getLast().getLegalMoves(board);
        board.displayBoardWithMoves(moves);
    }
}