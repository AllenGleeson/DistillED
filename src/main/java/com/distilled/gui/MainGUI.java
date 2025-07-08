package com.distilled.gui;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainGUI() {
        setTitle("DistillED Services");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add your GUI components here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}