package board;
import piece.Piece;

public abstract class Tile {
    public int index;
    public Piece piece;
    public boolean occupied;

    public Tile (int index, boolean occupied, Piece piece){
        this.index = index; this.occupied = occupied; this.piece = piece;
    }
}

class OccupiedTile extends Tile{
    public OccupiedTile(int index, Piece piece){
        super(index, true, piece);
    }
}

class EmptyTile extends Tile{
    public EmptyTile(int index){
        super(index, false, null);
    }
}
