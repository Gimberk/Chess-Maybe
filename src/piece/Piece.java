package piece;

import board.Board;
import board.Tile;

import java.util.List;

public abstract class Piece {
    public Type type;
    public int index;
    public List<Integer> directions;
    public Alliance alliance;
    public Tile tile;
    public boolean alive = true;

    public abstract List<Move> getLegalMoves(Board board);
}
