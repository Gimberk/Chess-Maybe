package engine.piece;

import engine.board.Board;
import engine.board.Tile;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

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
        final int tIdx = tile.index;
        final List<Move> moves = new ArrayList<>();
        for (int direction : directions){
            if (IsEdgeCase(tIdx, direction)) continue;
            if (abs(direction) == 16){
                if (moved) continue;
            }
            final int endIdx = tIdx + direction;
            if (endIdx >= board.tiles.size() || endIdx < 0) continue;

            final Tile tile = board.tiles.get(endIdx);
            if (!ValidateTile(tile)) continue;
            if (abs(direction) == 7 || abs(direction) == 9){
                if (!tile.occupied) continue;
            }
            final Move newMove = tile.occupied ?  new AttackMove(tIdx, endIdx, this, tile.piece) :
                    new MinorMove(tIdx, endIdx, this);
            moves.add(newMove);
        }
        return moves;
    }
}