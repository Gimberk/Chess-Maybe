package piece;

import board.Board;
import board.Tile;

import java.util.List;

public class King extends Piece {
    public King(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-9,-8,-7,-1,1,7,8,9);
        this.type = Type.KING; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        return null;
    }
}
