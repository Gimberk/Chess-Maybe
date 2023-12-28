package gui;

import javax.swing.*;
import java.awt.*;

public class Util {
    public static JButton createButton(final String text, final Color background, final int fontSize, final Color foreground, final boolean bold){
        JButton b = new JButton(text);
        b.setBackground(background);
        b.setForeground(foreground);
        b.setFocusPainted(false);
        int style = bold ? Font.BOLD : Font.PLAIN;
        b.setFont(new Font("Tahoma", style, fontSize));

        return b;
    }
}
