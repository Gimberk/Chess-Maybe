package piece;

import board.Board;
import board.Tile;

import java.util.List;

public class Rook extends Piece {
    public Rook(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-8,-1,1,8);
        this.type = Type.ROOK; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        return null;
    }
}