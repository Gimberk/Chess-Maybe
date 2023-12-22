package gui;

import engine.piece.Piece;
import  gui.Table;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static gui.Table.takenPanelColor;
import static gui.Table.takenPanelDimensions;

public class TakenPanel extends JPanel {
    private final Table table;

    TakenPanel(Table table){
        super(new GridLayout(30, 1));
        this.table = table;
        setBackground(takenPanelColor);
        setPreferredSize(takenPanelDimensions);
    }

    public void addPiece(Piece piece){
        try{
            final BufferedImage img =
                    ImageIO.read(new File(table.pieceIconPath + piece.alliance.toString().charAt(0) +
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