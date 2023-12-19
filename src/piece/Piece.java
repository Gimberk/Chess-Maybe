package piece;

import board.Board;
import java.util.List;

public abstract class Piece {
    public Type type;
    public int index;
    public List<Integer> directions;
    public Alliance alliance;

    public Piece(int index, List<Integer> directions, Alliance alliance, Type type){
        this.type = type; this.alliance = alliance; this.directions = directions; this.index = index;
    }

    public abstract List<Move> getLegalMoves(Board board);
}
