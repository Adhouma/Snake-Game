package com.learn.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

  // Fields
  private static final int SCREEN_WIDTH = 800;
  private static final int SCREEN_HEIGHT = 800;
  private static final int UNIT_SIZE = 25;
  private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
  private static final int GAME_SPEED = 75;
  private final int[] moveX = new int[GAME_UNITS];
  private final int[] moveY = new int[GAME_UNITS];
  private int snakeBodyPart = 5;
  private int applesEaten = 0;
  private int applePositionX;
  private int applePositionY;
  /**
   * Directions:
   * R: Right
   * L: Left
   * U: Up
   * D: Down
   *
   * Web begin the game with snake direction to the right
   */
  private char direction = 'R';
  private boolean running = false;
  private Timer timer;
  private final Random random;

  // Constructor
  public GamePanel() {
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.BLACK);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    startGame();
  }

  // Methods
  public void startGame() {
    showNewApple();
    running = true;
    timer = new Timer(GAME_SPEED, this);
    timer.start();
  }

  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    draw(graphics);
  }

  public void draw(Graphics graphics) {
    if (running) {
      // Set our Grid layout
      for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
        graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
      }
      // Draw apple
      graphics.setColor(Color.red);
      graphics.fillOval(applePositionX, applePositionY, UNIT_SIZE, UNIT_SIZE);
      // Draw snake
      for (int i = 0; i < snakeBodyPart; i++) {
        if (i == 0) {
          // Snake head color
          graphics.setColor(Color.yellow);
        } else {
          // Snake body color
          graphics.setColor(new Color(204, 204, 0));
        }

        graphics.fillRect(moveX[i], moveY[i], UNIT_SIZE, UNIT_SIZE);
      }

      // Score
      graphics.setColor(Color.RED);
      graphics.setFont(new Font("ink Free", Font.BOLD, 40));
      FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
      graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - fontMetrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 20);
    } else {
      gameOver(graphics);
    }
  }

  public void showNewApple() {
    applePositionX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
    applePositionY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
  }

  public void move() {
    for (int i = snakeBodyPart; i > 0; i--) {
      moveX[i] = moveX[i - 1];
      moveY[i] = moveY[i - 1];
    }

    switch (direction) {
      case 'U':
        moveY[0] = moveY[0] - UNIT_SIZE;
        break;
      case 'D':
        moveY[0] = moveY[0] + UNIT_SIZE;
        break;
      case 'L':
        moveX[0] = moveX[0] - UNIT_SIZE;
        break;
      case 'R':
        moveX[0] = moveX[0] + UNIT_SIZE;
        break;
    }
  }

  public void checkApple() {
    if (moveX[0] == applePositionX && moveY[0] == applePositionY) {
      applesEaten++;
      snakeBodyPart++;
      showNewApple();
    }
  }

  public void checkCollisions() {
    // Check if snake head collide with body
    for (int i = snakeBodyPart; i > 0; i--) {
      if ((moveX[0] == moveX[i]) && (moveY[0] == moveY[i])) {
        running = false;
      }
    }
    // Check if snake head touch left border
    if (moveX[0] < 0) {
      running = false;
    }
    // Check if snake head touch right border
    if (moveX[0] > SCREEN_WIDTH) {
      running = false;
    }
    // Check if snake head touch top border
    if (moveY[0] < 0) {
      running = false;
    }
    // Check if snake head touch bottom border
    if (moveY[0] > SCREEN_HEIGHT) {
      running = false;
    }

    if (!running) {
      timer.stop();
    }
  }

  public void gameOver(Graphics graphics) {
    // game over
    graphics.setColor(Color.RED);
    graphics.setFont(new Font("ink Free", Font.BOLD, 75));
    FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
    graphics.drawString("Game Over", (SCREEN_WIDTH - fontMetrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    // Score
    graphics.setColor(Color.RED);
    graphics.setFont(new Font("ink Free", Font.BOLD, 40));
    FontMetrics fontMetrics2 = getFontMetrics(graphics.getFont());
    graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - fontMetrics2.stringWidth("Game Over")) / 2, (SCREEN_HEIGHT / 2) + 50);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      checkApple();
      checkCollisions();
    }
    // Repaint the component
    this.repaint();
  }

  // Inner class
  public class MyKeyAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent event) {
      switch (event.getKeyCode()) {
        case KeyEvent.VK_RIGHT:
          if (direction != 'L') {
            direction = 'R';
          }
          break;
        case KeyEvent.VK_LEFT:
          if (direction != 'R') {
            direction = 'L';
          }
          break;
        case KeyEvent.VK_UP:
          if (direction != 'D') {
            direction = 'U';
          }
          break;
        case KeyEvent.VK_DOWN:
          if (direction != 'U') {
            direction = 'D';
          }
          break;
      }
    }

  }
}
