package main;

import javax.swing.*;
import java.awt.*;
import java.util.InputMismatchException;

public class GamePanel extends JPanel implements Runnable{
    final int ORIGINAL_TILE_SIZE = 32;
    final int SCALE = 2;
    final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    final int MAX_SCREEN_COLUMNS = 16;
    final int MAX_SCREEN_ROWS = 12;
    final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COLUMNS;
    final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROWS;
    final int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gammeThread;

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gammeThread = new Thread(this);
        gammeThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000_000_000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        /*long curT;
        long lastT = 0;
        long timer = 0;
        int count = 0;*/
        while (gammeThread != null) {
            /*curT = System.nanoTime();
            timer += (curT - lastT);
            lastT = curT;*/
            // 1 update character position
            update();
            // 2 draw screen with update
            repaint();
            //count++;
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime/= 1000_000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime+= drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*if (timer >= 1000_000_000) { get 60-61
                System.out.println(count);
                count = 0;
                timer = 0;
            }*/
        }
    }

    public void update() {
        if (keyHandler.up) {
            playerY-= playerSpeed;
        } else if (keyHandler.down) {
            playerY+= playerSpeed;
        } else if (keyHandler.left) {
            playerX-= playerSpeed;
        } else if (keyHandler.right) {
            playerX+= playerSpeed;
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(playerX, playerY, TILE_SIZE, TILE_SIZE);
        graphics2D.dispose();
    }
}
