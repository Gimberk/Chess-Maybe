package engine.piece;

public class AttackMove extends Move {
    public AttackMove(int start, int end, Piece piece, Piece takenPiece, boolean enPassant) {
        super(start, end, piece, takenPiece, enPassant);
    }
}
