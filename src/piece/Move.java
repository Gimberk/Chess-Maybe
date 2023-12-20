package piece;

import java.util.List;

public abstract class Move {
    public int start, end;
    Piece piece, takenPiece;

    public Move(int start, int end, Piece piece, Piece takenPiece){
        this.start = start; this.end = end; this.piece = piece; this.takenPiece = takenPiece;
    }

    public static boolean contains(int index, List<Move> moves){
        for (Move move : moves){
            if (move.end == index) return true;
        }
        return false;
    }
}

class MinorMove extends Move {

    public MinorMove(int start, int end, Piece piece) {
        super(start, end, piece, null);
    }
}

class AttackMove extends Move {

    public AttackMove(int start, int end, Piece piece, Piece takenPiece) {
        super(start, end, piece, takenPiece);
    }
}