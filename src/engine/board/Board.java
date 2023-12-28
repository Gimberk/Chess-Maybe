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

    public Player whitePlayer, blackPlayer;

    public boolean turn = true;
    public boolean whiteOnBottom = true;

    public Board() {
        generateTiles();
    }

    public static Board createStandardBoard(Player whitePlayer, Player blackPlayer) throws Exception {
        Board chessBoard = new Board();

        chessBoard.whitePlayer = whitePlayer;
        chessBoard.blackPlayer = blackPlayer;

        chessBoard.loadFen(Board.baseFen, true);
        chessBoard.displayBoard();
        return chessBoard;
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
            board.append(tile.toString());;
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

    public void clearBoard(){
        tiles.clear();
        generateTiles();
        pieces.clear();
    }

    public void loadFen(final String fen, final boolean whiteOnBottom) throws Exception {
        Map<Character, Type> pieces = Map.of(
                'k', Type.KING,
                'q', Type.QUEEN,
                'r', Type.ROOK,
                'n', Type.KNIGHT,
                'p', Type.PAWN,
                'b', Type.BISHOP);

        this.whiteOnBottom = whiteOnBottom;
        clearBoard();

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

    // Returns if the move was a success or not
    public boolean makeMove(final Move move) {
        if ((move.piece.alliance == Alliance.WHITE && !turn) || move.piece.alliance == Alliance.BLACK && turn)
            return false;

        final Tile start = tiles.get(move.start), end = tiles.get(move.end);
        final boolean attack = move.takenPiece != null;

        start.Update(null);
        end.Update(move.piece);

        move.piece.tile = end;
        if (attack) move.takenPiece.alive = false;

        if (move.enPassant){
            Tile enPassant = move.piece.alliance == Alliance.WHITE ? tiles.get(move.piece.tile.index + 8) :
                    tiles.get(move.piece.tile.index - 8);
            enPassant.piece.alive = false;
            move.takenPiece = enPassant.piece;
            enPassant.Update(null);
        }

        for (Piece piece : pieces) piece.justMoved = false;

        move.piece.moved = true; move.piece.justMoved = true;

        displayBoard();

        turn = !turn;
        return true;
    }

    public static boolean isFifthRank(final int index) { return index > 23 && index < 32; }
    public static boolean isFourthRank(final int index) { return index > 31 && index < 40; }
}
