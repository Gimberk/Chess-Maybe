package engine.piece;

import engine.board.Board;
import engine.board.Tile;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-17, -15, -10, -6, 6, 10, 15, 17);
        this.type = Type.KNIGHT; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public String toString(){
        char repr = 'N';
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
            final Move newMove = tile.occupied ?  new Move.AttackMove(tIdx, endIdx, this, tile.piece, false) :
                    new Move.MinorMove(tIdx, endIdx, this);
            moves.add(newMove);
        }
        return moves;
    }
}
