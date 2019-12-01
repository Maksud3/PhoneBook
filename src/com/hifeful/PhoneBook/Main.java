package com.hifeful.PhoneBook;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        EventQueue.invokeLater(() ->
        {
            var frame = new PhoneBookFrame();
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setIconImage(new ImageIcon(Main.class.getResource("/images/icon.png")).getImage());
            frame.setVisible(true);
        });
    }
}
