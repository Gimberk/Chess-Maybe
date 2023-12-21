package engine.piece;

import java.util.List;

public abstract class Move {
    public int start, end;
    public Piece piece, takenPiece;

    public Move(int start, int end, Piece piece, Piece takenPiece){
        this.start = start; this.end = end; this.piece = piece; this.takenPiece = takenPiece;
    }

    public boolean equals(Move other){
        return other.start == start && other.end == end && other.piece == piece;
    }

    public static boolean contains(int index, List<Move> moves){
        for (Move move : moves){
            if (move.end == index) return true;
        }
        return false;
    }
}

