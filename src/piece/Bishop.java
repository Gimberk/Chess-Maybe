package piece;

import board.Board;
import board.Tile;

import java.util.List;

public class Bishop extends Piece {
    public Bishop(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-9, -7, 7, 9);
        this.type = Type.BISHOP; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        return null;
    }
}