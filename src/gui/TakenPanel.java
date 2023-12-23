package gui;

import engine.piece.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static gui.GameFrame.takenPanelColor;
import static gui.GameFrame.takenPanelDimensions;

public class TakenPanel extends JPanel {
    private final GameFrame gameFrame;

    TakenPanel(GameFrame gameFrame){
        super(new GridLayout(30, 1));
        this.gameFrame = gameFrame;
        setBackground(takenPanelColor);
        setPreferredSize(takenPanelDimensions);
    }

    public void addPiece(Piece piece){
        try{
            final BufferedImage img =
                    ImageIO.read(new File(gameFrame.pieceIconPath + piece.alliance.toString().charAt(0) +
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