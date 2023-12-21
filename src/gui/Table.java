package gui;

import engine.board.*;
import engine.piece.AttackMove;
import engine.piece.MinorMove;
import engine.piece.Move;
import engine.piece.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame frame;

    private final String pieceIconPath;

    private final BoardPanel boardPanel;
    private final TakenPanel takenPanel;

    private Piece selectedPiece;
    private TilePanel selectedPanel;

    private final Board chessBoard;

    private final Color lightTileColor = new Color(235, 212, 145);
    private final Color darkTileColor = new Color(125, 70, 22);
    private final Color highlightedLightTileColor = new Color(153, 134, 83);
    private final Color highlightedDarkTileColor = new Color(79, 42, 10);
    private final Color selectedTileColor = new Color(168, 29, 22);
    private final Color takenPanelColor = new Color(189, 154, 102);

    private final static Dimension outerFrameDimensions = new Dimension(600, 600);
    private final static Dimension takenPanelDimensions = new Dimension(40, 600);
    private final static Dimension boardPanelDimensions = new Dimension(400, 350);
    private final static Dimension tilePanelDimensions = new Dimension(10, 10);

    public Table(String pieceSet) throws Exception {
        pieceIconPath = "assets/" + pieceSet + "/";
        chessBoard = new Board();
        chessBoard.loadFen(Board.baseFen, true);
        chessBoard.displayBoard();

        frame = new JFrame("A chess game in Java that samuek will lose");
        frame.setLayout(new BorderLayout());
        frame.setSize(outerFrameDimensions);

        final JMenuBar menuBar = populateMenuBar();
        frame.setJMenuBar(menuBar);

        takenPanel = new TakenPanel();
        frame.add(takenPanel, BorderLayout.WEST);

        boardPanel = new BoardPanel();
        frame.add(boardPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JMenuBar populateMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Not implemented yet!");
            }
        });
        fileMenu.add(openPGN);

        return fileMenu;
    }

    private class TakenPanel extends JPanel {
        TakenPanel(){
            super(new GridLayout(30, 1));
            setBackground(takenPanelColor);
            setPreferredSize(takenPanelDimensions);
        }

        public void addPiece(Piece piece){
            try{
                final BufferedImage img =
                        ImageIO.read(new File(pieceIconPath + piece.alliance.toString().charAt(0) +
                                piece + ".gif"));
                add(new JLabel(new ImageIcon(img)));
            }
            catch (IOException e){
                e.printStackTrace();
            }
            validate();
            repaint();
        }
    }

    private class BoardPanel extends JPanel {
        private final List<TilePanel> boardTiles;
        BoardPanel() {
            super(new GridLayout(8,8));
            boardTiles = new ArrayList<>();

            boolean color = true;
            int counter = 0;
            for (int i = 0; i < Board.numTiles; i++){
                final TilePanel tile = new TilePanel(this, i, color);
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

        public void drawBoard(final Board chessBoard) {
            removeAll();
            for (final TilePanel tile : boardTiles){
                tile.redrawTile(chessBoard);
                add(tile);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel {
        private final int index;
        private final boolean color;

        private TilePanel get(){
            return this;
        }

        TilePanel(final BoardPanel panel, final int index, final boolean color){
            super(new GridLayout());
            this.index = index;
            setPreferredSize(tilePanelDimensions);
            this.color = color;
            assignTileColor(color ? lightTileColor : darkTileColor);
            assignPieceIcon();
            validate();

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    final Tile tile = chessBoard.tiles.get(index);
                    if(isRightMouseButton(e)){
                        selectedPiece = null;
                        if (selectedPanel != null){
                            selectedPanel = null;
                        }
                        boardPanel.drawBoard(chessBoard);
                    }
                    else if (isLeftMouseButton(e)){
                        if (selectedPiece == null){
                            if (tile.piece != null){
                                assignTileColor(color ? highlightedLightTileColor : highlightedDarkTileColor);
                                boardPanel.highlightLegalMoves(tile.piece.getLegalMoves(chessBoard));
                            }
                            selectedPiece = tile.piece;
                            selectedPanel = get();
                        }
                        else{
                            Tile source = selectedPiece.tile;
                            Move move = source.occupied ?  new
                                    AttackMove(source.index, tile.index, selectedPiece, tile.piece) :
                                    new MinorMove(source.index, tile.index, selectedPiece);
                            if (move.start == move.end) return;
                            boolean moveSuccess = chessBoard.MakeMove(move);
                            if (moveSuccess){
                                selectedPiece = null;
                                selectedPanel.assignTileColor(selectedPanel.color ? lightTileColor : darkTileColor);
                                selectedPanel = null;
                                if (move.takenPiece != null) takenPanel.addPiece(move.takenPiece);
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        boardPanel.drawBoard(chessBoard);
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

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }

        private void assignPieceIcon(){
            removeAll();
            if (!chessBoard.tiles.get(index).occupied) return;
            try{
                final BufferedImage img =
                    ImageIO.read(new File(pieceIconPath +
                        chessBoard.tiles.get(index).piece.alliance.toString().charAt(0) +
                        chessBoard.tiles.get(index).piece.toString() + ".gif"));
                add(new JLabel(new ImageIcon(img)));
        }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        private void assignTileColor(final Color color) {
            setBackground(color);
        }

        public void redrawTile(final Board chessBoard) {
            assignPieceIcon();
            assignTileColor(color ? lightTileColor : darkTileColor);
            validate();
            repaint();
        }
    }
}
