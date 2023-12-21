package engine.board;
import engine.piece.Piece;

public class Tile {
    public final int index;
    public Piece piece;
    public boolean occupied;

    public Tile (int index, boolean occupied, Piece piece){
        this.index = index; this.occupied = occupied; this.piece = piece;
    }

    public void Update(Piece piece){
        this.piece = piece; occupied = piece != null;
    }

    @Override
    public String toString(){
        return occupied ? piece.toString() : "-";
    }
}