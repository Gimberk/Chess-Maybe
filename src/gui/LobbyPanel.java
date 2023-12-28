package gui;

import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.GameFrame.lobbyPanelColor;
import static gui.GameFrame.lobbyPanelDimensions;

public class LobbyPanel extends JPanel {
    private final GameFrame main;

    public LobbyPanel(GameFrame frame){
        super(new GridLayout(0,1));
        int margin = 400;

        main = frame;

        setBackground(lobbyPanelColor);
        setPreferredSize(lobbyPanelDimensions);

        createMenu();
    }

    private void createMenu() {
        JLabel title = new JLabel("Flamingo", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 100));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(lobbyPanelColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JButton start = Util.createButton("Start A Game", new Color(66, 103, 178), 75, new Color(255,255,255), true);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.startGame();
            }
        });

        add(title);
        add(buttonPanel);
        buttonPanel.add(start);
    }
}
