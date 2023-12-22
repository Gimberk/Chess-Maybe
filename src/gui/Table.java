package gui;

import engine.board.*;
import engine.piece.*;

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
    // Planned GUI features:
    // FLip board
    // Load PGN
    // Load FEN
    // Log Moves
    // Show/Hide Debug Panel
    // Implement Debug Panel
    // Show/Hide Legal Moves
    // Start Btn


    private final JFrame frame;

    private final String pieceIconPath;

    private BoardPanel boardPanel;
    private TakenPanel takenPanel;

    private Piece selectedPiece;
    private TilePanel selectedPanel;

    private Board chessBoard;

    private final Color lightTileColor = new Color(235, 212, 145);
    private final Color darkTileColor = new Color(125, 70, 22);
    private final Color highlightedLightTileColor = new Color(153, 134, 83);
    private final Color highlightedDarkTileColor = new Color(79, 42, 10);
    private final Color selectedTileColor = new Color(168, 29, 22);
    private final Color takenPanelColor = new Color(189, 154, 102);
    private final Color hoverLightTileColor = new Color(189, 168, 111);
    private final Color hoverDarkTileColor = new Color(107, 60, 18);

    private final static Dimension outerFrameDimensions = new Dimension(600, 600);
    private final static Dimension takenPanelDimensions = new Dimension(40, 600);
    private final static Dimension boardPanelDimensions = new Dimension(400, 350);
    private final static Dimension tilePanelDimensions = new Dimension(10, 10);

    private boolean showTileIndices = false;
    private boolean gameStarted = false;

    public Table(String pieceSet) throws Exception {
        pieceIconPath = "assets/" + pieceSet + "/";
        chessBoard = Board.createStandardBoard();

        frame = createGameFrame();
    }

    private JFrame createGameFrame(){
        final JFrame frame = new JFrame("A chess game in Java that samuek will lose");
        frame.setLayout(new BorderLayout());
        frame.setSize(outerFrameDimensions);

        final JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
        return frame;
    }

    private void startGame(){
        if (gameStarted){
            System.out.println("Game Already Started!");
            return;
        }
        gameStarted = true;
        takenPanel = new TakenPanel();
        frame.add(takenPanel, BorderLayout.WEST);

        boardPanel = new BoardPanel();
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.repaint();
        frame.validate();
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createGameMenu());
        menuBar.add(createDebugMenu());

        return menuBar;
    }

    private JMenu createGameMenu() {
        final JMenu gameMenu = new JMenu("Game");

        final JMenuItem start = new JMenuItem("Start Game!");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        gameMenu.add(start);
        return gameMenu;
    }

    private JMenu createDebugMenu() {
        final JMenu debugMenu = new JMenu("Debug");

        final JCheckBoxMenuItem tileIndices = new JCheckBoxMenuItem("Show Tile Indices");
        tileIndices.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTileIndices = !showTileIndices;
                if (!gameStarted) return;
                boardPanel.redrawBoard(chessBoard);
            }
        });

        debugMenu.add(tileIndices);
        return debugMenu;
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
                final TilePanel tile = new TilePanel(i, color);
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

    private class TilePanel extends JPanel {
        private final int index;
        private final boolean color;

        private TilePanel get(){
            return this;
        }

        TilePanel(final int index, final boolean color){
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
                        boardPanel.redrawBoard(chessBoard);
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
                                    AttackMove(source.index, tile.index, selectedPiece, tile.piece, false) :
                                    new MinorMove(source.index, tile.index, selectedPiece);

                            if (move.start == move.end) return;

                            if (!Move.contains(move.end, move.piece.getLegalMoves(chessBoard))) return;
                            for (Move legal : move.piece.getLegalMoves(chessBoard)){
                                if (legal.equals(move)){
                                    move = legal;
                                    break;
                                }
                            }

                            boolean moveSuccess = chessBoard.MakeMove(move);
                            if (moveSuccess){
                                selectedPiece = null;
                                selectedPanel.assignTileColor(selectedPanel.color ? lightTileColor : darkTileColor);
                                selectedPanel = null;

                                Tile enPassantTile = move.piece.alliance == Alliance.WHITE ?
                                        chessBoard.tiles.get(move.end+8) : chessBoard.tiles.get(move.end-8);
                                if (move.takenPiece != null || move.enPassant) {
                                    // While the pawn class doesn't set taken piece to anything in an en passant move,
                                    // i updated the make move function to set it because it was the simplest solution
                                    // I could think of
                                    assert move.takenPiece != null;
                                    takenPanel.addPiece(move.takenPiece);
                                }
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        boardPanel.redrawBoard(chessBoard);
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
            if (showTileIndices){
                add(new JLabel(String.valueOf(index)));
                return;
            }
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
