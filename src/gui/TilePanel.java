package gui;

import engine.board.Board;
import engine.board.Player;
import engine.board.Tile;
import engine.piece.Alliance;
import engine.piece.Move;
import engine.piece.Move;
import engine.piece.Type;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static gui.GameFrame.*;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class TilePanel extends JPanel {
    private final int index;
    private final boolean color;

    private TilePanel get(){
        return this;
    }

    private final GameFrame gameFrame;

    TilePanel(final int index, final boolean color, final GameFrame gameFrame){
        super(new GridLayout());
        this.gameFrame = gameFrame;
        this.index = index;
        setPreferredSize(tilePanelDimensions);
        this.color = color;
        assignTileColor(color ? lightTileColor : darkTileColor);
        assignPieceIcon();
        validate();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final Tile tile = gameFrame.chessBoard.tiles.get(index);
                if ((gameFrame.chessBoard.turn && gameFrame.chessBoard.whitePlayer == Player.AI) ||
                        (!gameFrame.chessBoard.turn && gameFrame.chessBoard.blackPlayer == Player.AI)) return;
                if(isRightMouseButton(e)){
                    gameFrame.selectedPiece = null;
                    if (gameFrame.selectedPanel != null){
                        gameFrame.selectedPanel = null;
                    }
                    gameFrame.boardPanel.redrawBoard(gameFrame.chessBoard);
                }
                else if (isLeftMouseButton(e)){
                    if (gameFrame.selectedPiece == null){
                        if (tile.piece != null){
                            assignTileColor(color ? highlightedLightTileColor : highlightedDarkTileColor);
                            gameFrame.boardPanel.highlightLegalMoves(tile.piece.getLegalMoves(gameFrame.chessBoard));
                        }
                        gameFrame.selectedPiece = tile.piece;
                        gameFrame.selectedPanel = get();
                    }
                    else{
                        Tile source = gameFrame.selectedPiece.tile;
                        Move move = source.occupied ?
                                new Move.AttackMove(source.index, tile.index, gameFrame.selectedPiece, tile.piece, false) :
                                new Move.MinorMove(source.index, tile.index, gameFrame.selectedPiece);

                        if (move.start == move.end) return;

                        if (!Move.contains(move.end, move.piece.getLegalMoves(gameFrame.chessBoard))) return;
                        for (Move legal : move.piece.getLegalMoves(gameFrame.chessBoard)){
                            if (legal.equals(move)){
                                move = legal;
                                break;
                            }
                        }

                        boolean moveSuccess = gameFrame.chessBoard.makeMove(move);
                        if (moveSuccess){
                            gameFrame.selectedPiece = null;
                            gameFrame.selectedPanel.assignTileColor(gameFrame.selectedPanel.color ? lightTileColor : darkTileColor);
                            gameFrame.selectedPanel = null;

                            if (move.takenPiece != null || (move.piece.type == Type.PAWN && move.enPassant)) {
                                // While the pawn class doesn't set taken piece to anything in an en passant move,
                                // i updated the make move function to set it because it was the simplest solution
                                // I could think of
                                assert move.takenPiece != null;
                                gameFrame.takenPanel.addPiece(move.takenPiece);
                            }
                            gameFrame.boardPanel.redrawBoard(gameFrame.chessBoard);
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
        if (gameFrame.showTileIndices){
            add(new JLabel(String.valueOf(index)));
            return;
        }
        if (!gameFrame.chessBoard.tiles.get(index).occupied) return;
        try{
            final BufferedImage img =
                    ImageIO.read(new File(gameFrame.pieceIconPath +
                            gameFrame.chessBoard.tiles.get(index).piece.alliance.toString().charAt(0) +
                            gameFrame.chessBoard.tiles.get(index).piece.toString() + ".gif"));
            add(new JLabel(new ImageIcon(getScaledImage(img, 75, 75))));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private Image getScaledImage(Image source, int width, int height){
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(source, 0, 0, width, height, null);
        g2.dispose();

        return resizedImg;
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