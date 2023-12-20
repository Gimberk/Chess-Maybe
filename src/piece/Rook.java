package piece;

import board.Board;
import board.Tile;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-8,-1,1,8);
        this.type = Type.ROOK; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public String toString(){
        char repr = 'R';
        return alliance == Alliance.WHITE ? String.valueOf(repr) : String.valueOf(Character.toLowerCase(repr));
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        final int tIdx = tile.index;
        final List<Move> moves = new ArrayList<>();
        for (int direction : directions){
            int endIdx = tIdx+direction;
            if (IsEdgeCase(tIdx, direction)) continue;
            while (endIdx < board.tiles.size() && endIdx >= 0){
                final Tile tile = board.tiles.get(endIdx);
                if (!ValidateTile(tile)) break;
                Move move = tile.occupied ? new AttackMove(tIdx, endIdx, this, tile.piece) :
                        new MinorMove(tIdx, endIdx, this);
                moves.add(move);
                if (IsEdgeCase(endIdx, direction)) break;
                endIdx += direction;
            }
        }
        return moves;
    }
}