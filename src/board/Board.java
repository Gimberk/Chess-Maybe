package board;

import piece.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board {
    final List<Tile> tiles = new ArrayList<>();
    final List<Piece> pieces = new ArrayList<>();

    public static final String baseFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    public Board() {
        generateTiles();
    }

    public void loadFen(final String fen) throws Exception {
        Map<Character, Type> pieces = Map.of(
                'k', Type.KING,
                'q', Type.QUEEN,
                'r', Type.ROOK,
                'n', Type.KNIGHT,
                'p', Type.PAWN,
                'b', Type.BISHOP);

        int file = 0, rank = 7;
        for (char symbol : fen.toCharArray())
        {
            if (symbol == '/')
            {
                file = 0;
                rank--;
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

                    PlacePiece(type, alliance, tiles.get(index));
                    file++;
                }
            }
        }
    }

    private void PlacePiece(final Type type, final Alliance alliance, final Tile tile) throws Exception {
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
}
