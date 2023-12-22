package engine.piece;

import engine.board.Board;
import engine.board.Tile;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-9,-8,-7,-1,1,7,8,9);
        this.type = Type.KING; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public String toString(){
        char repr = 'K';
        return alliance == Alliance.WHITE ? String.valueOf(repr) : String.valueOf(Character.toLowerCase(repr));
    }

    @Override
    public List<Move> getLegalMoves(Board board){
        final int tIdx = tile.index;
        final List<Move> moves = new ArrayList<>();
        for (int direction : directions){
            if (IsEdgeCase(tIdx, direction)) continue;
            final int endIdx = tIdx + direction;
            if (endIdx >= board.tiles.size() || endIdx < 0) continue;

            final Tile tile = board.tiles.get(endIdx);
            if (!ValidateTile(tile)) continue;
            final Move newMove = tile.occupied ?  new AttackMove(tIdx, endIdx, this, tile.piece, false) :
                    new MinorMove(tIdx, endIdx, this);
            moves.add(newMove);
        }
        return moves;
    }
}
