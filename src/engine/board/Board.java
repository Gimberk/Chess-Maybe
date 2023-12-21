package engine.board;

import engine.piece.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board {
    public final List<Tile> tiles = new ArrayList<>();
    public final List<Piece> pieces = new ArrayList<>();

    public static final int numTiles = 64;
    public static final String baseFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    public Board() {
        generateTiles();
    }

    public void displayBoard(){
        final StringBuilder board = new StringBuilder();
        int file = 0, index = 7;
        board.append("\n8 ");

        for (final Tile tile : tiles)
        {
            if (file > 7)
            {
                file = 0;
                board.append('\n');
                board.append(index).append(' ');
                index--;
            }
            board.append(tile.toString());
            board.append(' ');
            file++;
        }

        board.append("\n  a b c d e f g h");

        System.out.println(board);
    }

    public void displayBoardWithMoves(List<Move> moves){
        final StringBuilder board = new StringBuilder();
        int file = 0, index = 7;
        board.append("\n8 ");

        for (final Tile tile : tiles)
        {
            if (file > 7)
            {
                file = 0;
                board.append('\n');
                board.append(index).append(' ');
                index--;
            }
            if (Move.contains(tile.index, moves)) board.append('@');
            else board.append(tile.toString());
            board.append(' ');
            file++;
        }

        board.append("\n  a b c d e f g h");

        System.out.println(board);
    }

    public void loadFen(final String fen, final boolean whiteOnBottom) throws Exception {
        Map<Character, Type> pieces = Map.of(
                'k', Type.KING,
                'q', Type.QUEEN,
                'r', Type.ROOK,
                'n', Type.KNIGHT,
                'p', Type.PAWN,
                'b', Type.BISHOP);

        int file = 0, rank = whiteOnBottom ? 0 : 7;
        for (char symbol : fen.toCharArray())
        {
            if (symbol == '/')
            {
                file = 0;
                rank = whiteOnBottom ? rank + 1 : rank - 1;
            }
            else
            {
                if (Character.isDigit(symbol))
                    file += Character.getNumericValue(symbol);
                else
                {
                    final int index = rank * 8 + file;

                    final Alliance alliance = Character.isUpperCase(symbol) ? Alliance.WHITE : Alliance.BLACK;
                    final Type type = pieces.get(Character.toLowerCase(symbol));

                    placePiece(type, alliance, tiles.get(index));
                    file++;
                }
            }
        }
    }

    private void placePiece(final Type type, final Alliance alliance, final Tile tile) throws Exception {
        Piece piece;
        final int idx = pieces.size();
        switch (type){
            case KING:
                piece = new King(idx, alliance, tile);
                tile.Update(piece);
                break;
            case QUEEN:
                piece = new Queen(idx, alliance, tile);
                tile.Update(piece);
                break;
            case ROOK:
                piece = new Rook(idx, alliance, tile);
                tile.Update(piece);
                break;
            case BISHOP:
                piece = new Bishop(idx, alliance, tile);
                tile.Update(piece);
                break;
            case PAWN:
                piece = new Pawn(idx, alliance, tile);
                tile.Update(piece);
                break;
            case KNIGHT:
                piece = new Knight(idx, alliance, tile);
                tile.Update(piece);
                break;
            default:
                throw new Exception("Invalid Piece Type When Initializing Piece");
        }

        pieces.add(piece);
    }

    private void generateTiles() {
        for (int i = 0; i < 64; i++){
            Tile tile = new Tile(i, false, null);
            tiles.add(tile);
        }
    }

    public boolean MakeMove(Move move){
        final List<Move> legals = move.piece.getLegalMoves(this);
        if (!Move.contains(move.end, legals)){
            System.out.println("Illegal Move!");
            return false;
        }

        final Tile start = tiles.get(move.start), end = tiles.get(move.end);
        final boolean attack = move.takenPiece != null;

        start.Update(null);
        end.Update(move.piece);

        move.piece.tile = end;
        if (attack) move.takenPiece.alive = false;

        displayBoard();
        return true;
    }
}
