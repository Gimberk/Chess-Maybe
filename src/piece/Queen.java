package piece;

import board.Board;
import board.Tile;

import java.util.List;

public class Queen extends Piece {
    public Queen(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-9,-8,-7,-1,1,7,8,9);
        this.type = Type.QUEEN; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        return null;
    }
}