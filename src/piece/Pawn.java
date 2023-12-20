package piece;

import board.Board;
import board.Tile;

import java.util.List;

public class Pawn extends Piece {
    public Pawn(int index, Alliance alliance, Tile tile){
        this.directions = List.of(8,-8,-7,7,-9,9,16,-16);
        this.type = Type.PAWN; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public String toString(){
        char repr = 'P';
        return alliance == Alliance.WHITE ? String.valueOf(repr) : String.valueOf(Character.toLowerCase(repr));
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        return null;
    }
}