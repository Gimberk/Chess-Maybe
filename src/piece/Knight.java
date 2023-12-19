package piece;

import board.Board;
import board.Tile;

import java.util.List;

public class Knight extends Piece {
    public Knight(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-17, -15, -10, -6, 6, 10, 15, 17);
        this.type = Type.KNIGHT; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        return null;
    }
}
