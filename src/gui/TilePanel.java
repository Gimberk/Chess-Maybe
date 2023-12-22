package gui;

import engine.board.Board;
import engine.board.Tile;
import engine.piece.Alliance;
import engine.piece.AttackMove;
import engine.piece.MinorMove;
import engine.piece.Move;

import gui.Table;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static gui.Table.*;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class TilePanel extends JPanel {
    private final int index;
    private final boolean color;

    private TilePanel get(){
        return this;
    }

    private final Table table;

    TilePanel(final int index, final boolean color, final Table table){
        super(new GridLayout());
        this.table = table;
        this.index = index;
        setPreferredSize(tilePanelDimensions);
        this.color = color;
        assignTileColor(color ? lightTileColor : darkTileColor);
        assignPieceIcon();
        validate();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final Tile tile = table.chessBoard.tiles.get(index);
                if(isRightMouseButton(e)){
                    table.selectedPiece = null;
                    if (table.selectedPanel != null){
                        table.selectedPanel = null;
                    }
                    table.boardPanel.redrawBoard(table.chessBoard);
                }
                else if (isLeftMouseButton(e)){
                    if (table.selectedPiece == null){
                        if (tile.piece != null){
                            assignTileColor(color ? highlightedLightTileColor : highlightedDarkTileColor);
                            table.boardPanel.highlightLegalMoves(tile.piece.getLegalMoves(table.chessBoard));
                        }
                        table.selectedPiece = tile.piece;
                        table.selectedPanel = get();
                    }
                    else{
                        Tile source = table.selectedPiece.tile;
                        Move move = source.occupied ?  new
                                AttackMove(source.index, tile.index, table.selectedPiece, tile.piece, false) :
                                new MinorMove(source.index, tile.index, table.selectedPiece);

                        if (move.start == move.end) return;

                        if (!Move.contains(move.end, move.piece.getLegalMoves(table.chessBoard))) return;
                        for (Move legal : move.piece.getLegalMoves(table.chessBoard)){
                            if (legal.equals(move)){
                                move = legal;
                                break;
                            }
                        }

                        boolean moveSuccess = table.chessBoard.makeMove(move);
                        if (moveSuccess){
                            table.selectedPiece = null;
                            table.selectedPanel.assignTileColor(table.selectedPanel.color ? lightTileColor : darkTileColor);
                            table.selectedPanel = null;

                            Tile enPassantTile = move.piece.alliance == Alliance.WHITE ?
                                    table.chessBoard.tiles.get(move.end+8) : table.chessBoard.tiles.get(move.end-8);
                            if (move.takenPiece != null || move.enPassant) {
                                // While the pawn class doesn't set taken piece to anything in an en passant move,
                                // i updated the make move function to set it because it was the simplest solution
                                // I could think of
                                assert move.takenPiece != null;
                                table.takenPanel.addPiece(move.takenPiece);
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    table.boardPanel.redrawBoard(table.chessBoard);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (getBackground() == highlightedLightTileColor || getBackground() == highlightedDarkTileColor)
                    return;
                if (getBackground() != selectedTileColor)
                    assignTileColor(color ? hoverLightTileColor : hoverDarkTileColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Don't highlight this tile if it is a highlighted legal move
                if (getBackground() == highlightedLightTileColor || getBackground() == highlightedDarkTileColor)
                    return;
                if (getBackground() != selectedTileColor)
                    assignTileColor(color ? lightTileColor : darkTileColor);
            }
        });
    }

    private void assignPieceIcon(){
        removeAll();
        if (table.showTileIndices){
            add(new JLabel(String.valueOf(index)));
            return;
        }
        if (!table.chessBoard.tiles.get(index).occupied) return;
        try{
            final BufferedImage img =
                    ImageIO.read(new File(table.pieceIconPath +
                            table.chessBoard.tiles.get(index).piece.alliance.toString().charAt(0) +
                            table.chessBoard.tiles.get(index).piece.toString() + ".gif"));
            add(new JLabel(new ImageIcon(img)));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void assignTileColor(final Color color) {
        setBackground(color);
    }

    public void redrawTile(final Board chessBoard) {
        assignPieceIcon();
        assignTileColor(color ? lightTileColor : darkTileColor);
        validate();
        repaint();
    }
}