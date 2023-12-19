import board.Board;

public class Chess {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        board.loadFen(Board.baseFen);
    }
}