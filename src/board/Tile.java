package board;
import piece.Piece;

public class Tile {
    public int index;
    public Piece piece;
    public boolean occupied;

    public Tile (int index, boolean occupied, Piece piece){
        this.index = index; this.occupied = occupied; this.piece = piece;
    }

    public void Update(Piece piece){
        this.piece = piece; occupied = piece != null;
    }
}