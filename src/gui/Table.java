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


    public final JFrame frame;

    public final String pieceIconPath;

    public BoardPanel boardPanel;
    public TakenPanel takenPanel;

    public Piece selectedPiece;
    public TilePanel selectedPanel;

    public Board chessBoard;

    public static final Color lightTileColor = new Color(235, 212, 145);
    public static final Color darkTileColor = new Color(125, 70, 22);
    public static final Color highlightedLightTileColor = new Color(153, 134, 83);
    public static final Color highlightedDarkTileColor = new Color(79, 42, 10);
    public static final Color selectedTileColor = new Color(168, 29, 22);
    public static final Color takenPanelColor = new Color(189, 154, 102);
    public static final Color hoverLightTileColor = new Color(189, 168, 111);
    public static final Color hoverDarkTileColor = new Color(107, 60, 18);

    public final static Dimension outerFrameDimensions = new Dimension(600, 600);
    public final static Dimension takenPanelDimensions = new Dimension(40, 600);
    public final static Dimension boardPanelDimensions = new Dimension(400, 350);
    public final static Dimension tilePanelDimensions = new Dimension(10, 10);

    public boolean showTileIndices = false;
    public boolean gameStarted = false;

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
        takenPanel = new TakenPanel(this);
        frame.add(takenPanel, BorderLayout.WEST);

        boardPanel = new BoardPanel(this);
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
}