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
            if (alliance == Alliance.WHITE){
                if (direction > 0) continue;
            }
            else{
                if (direction < 0) continue;
            }
            if (IsEdgeCase(tIdx, direction)) continue;
            if (abs(direction) == 16){
                if (moved) continue;
                if (alliance == Alliance.WHITE){
                    if (board.tiles.get(tIdx-8).occupied) continue;
                }
                else{
                    if (board.tiles.get(tIdx+8).occupied) continue;
                }
            }

            final int endIdx = tIdx + direction;
            if (endIdx >= board.tiles.size() || endIdx < 0) continue;
            final Tile tile = board.tiles.get(endIdx);

            if (abs(direction) == 16 || abs(direction) == 8){
                if (tile.occupied) continue;
            }

            if (!ValidateTile(tile)) continue;
            boolean enPassantMove = false;
            if (abs(direction) == 7 || abs(direction) == 9){
                if (!tile.occupied){
                    Tile enPassantTile;
                    enPassantTile = alliance == Alliance.WHITE ? board.tiles.get(endIdx+8) : board.tiles.get(endIdx-8);
                    if (alliance == Alliance.WHITE && !Board.isFifthRank(enPassantTile.index) ||
                            (alliance == Alliance.BLACK && !Board.isFourthRank(enPassantTile.index))) continue;
                    if ((enPassantTile.occupied && enPassantTile.piece.justMoved)) enPassantMove = true;
                    else continue;
                }
            }
            final Move newMove;
            if (enPassantMove) newMove = new Move.AttackMove(tIdx, endIdx, this, tile.piece, enPassantMove);
            else newMove = tile.occupied ?  new Move.AttackMove(tIdx, endIdx, this, tile.piece, enPassantMove) :
                    new Move.MinorMove(tIdx, endIdx, this);
            moves.add(newMove);
        }
        return moves;
    }
}