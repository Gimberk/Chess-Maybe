package gui;

import javax.swing.*;
import java.awt.*;

import static gui.GameFrame.moveLogPanelColor;
import static gui.GameFrame.moveLogPanelDimensions;

public class MovePanel extends JPanel {
    public MovePanel(final GameFrame frame){
        super(new GridLayout());
        setBackground(moveLogPanelColor);
        setPreferredSize(moveLogPanelDimensions);
    }
}
