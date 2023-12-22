package gui;

import engine.board.Board;
import engine.piece.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static gui.Table.boardPanelDimensions;
import static gui.Table.selectedTileColor;

public class BoardPanel extends JPanel {
    private final List<TilePanel> boardTiles;

    BoardPanel(final Table table) {
        super(new GridLayout(8,8));
        boardTiles = new ArrayList<>();

        boolean color = true;
        int counter = 0;
        for (int i = 0; i < Board.numTiles; i++){
            final TilePanel tile = new TilePanel(i, color, table);
            boardTiles.add(tile);
            add(tile);
            color = !color;
            counter++;
            if (counter == 8){
                color = !color;
                counter = 0;
            }
        }
        setPreferredSize(boardPanelDimensions);
        validate();
    }

    public void highlightLegalMoves(List<Move> moves){
        for (Move move : moves){
            TilePanel tile = boardTiles.get(move.end);
            tile.assignTileColor(selectedTileColor);
        }
    }

    public void redrawBoard(final Board chessBoard) {
        removeAll();
        for (final TilePanel tile : boardTiles){
            tile.redrawTile(chessBoard);
            add(tile);
        }
        validate();
        repaint();
    }
}