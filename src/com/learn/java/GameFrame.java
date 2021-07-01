package com.learn.java;

import javax.swing.*;

public class GameFrame extends JFrame {

  // Constructor
  public GameFrame() {
    this.add(new GamePanel());
    this.setTitle("Snake Game");
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setResizable(false);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
}
