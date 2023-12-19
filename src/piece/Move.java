package piece;

public abstract class Move {
    public int start, end;
    Piece piece, takenPiece;

    public Move(int start, int end, Piece piece, Piece takenPiece){
        this.start = start; this.end = end; this.piece = piece; this.takenPiece = takenPiece;
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