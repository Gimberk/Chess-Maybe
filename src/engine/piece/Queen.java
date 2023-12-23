package engine.piece;

import engine.board.Board;
import engine.board.Tile;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(int index, Alliance alliance, Tile tile){
        this.directions = List.of(-9,-8,-7,-1,1,7,8,9);
        this.type = Type.QUEEN; this.alliance = alliance; this.index = index; this.tile = tile;
    }

    @Override
    public String toString(){
        char repr = 'Q';
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
                Move move = tile.occupied ? new Move.AttackMove(tIdx, endIdx, this, tile.piece, false) :
                        new Move.MinorMove(tIdx, endIdx, this);
                moves.add(move);
                if (IsEdgeCase(endIdx, direction) || (move.takenPiece != null && move.takenPiece.alliance != alliance))
                    break;
                endIdx += direction;
            }
        }
        return moves;
    }
}