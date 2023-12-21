package engine.piece;

import engine.board.Board;
import engine.board.Tile;

import java.util.List;

public abstract class Piece {
    public Type type;
    public int index;
    public List<Integer> directions;
    public Alliance alliance;
    public Tile tile;
    public boolean moved = false;
    public boolean justMoved = false;
    public boolean alive = true;

    public boolean ValidateTile(Tile t){
        if (!t.occupied) return true;
        return t.piece.alliance != alliance;
    }

    public boolean IsEdgeCase(int tIdx, int direction){
        boolean left = tIdx % 8 == 0;
        boolean right = (tIdx+1)%8==0;
        boolean second = (tIdx-1) % 8 == 0;
        boolean seventh = (tIdx+2)%8==0;

        if (left){
            return direction == -1 || direction == 7 || direction == -9 || direction == 6 || direction == 15
                    || direction == -10 || direction == -17;
        }
        else if (right){
            return direction == 1 || direction == -7 || direction == 9 || direction == -6 || direction == -15
                    || direction == 10 || direction == 17;
        }
        else if (seventh){
            return direction == -6 || direction == 10;
        }
        else if (second){
            return direction == 6 || direction == -10;
        }
        return false;
    }

    public abstract List<Move> getLegalMoves(Board board);
}
