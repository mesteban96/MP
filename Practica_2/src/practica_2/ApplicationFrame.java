/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ivanm
 */
public class ApplicationFrame extends JFrame {

    private JPanel panel;
    private GridLayout fieldLayout;

    private int cols, rows;

    private int panelSize;
    private final int gap = 1;

    private int[] speed = new int[2];
    private int snakeSize;

    private ArrayList<Point> snake;

    private SnakeMover snakeMover;

    private ArrayList<JPanel> gameCells;

    private Point reward;

    public ApplicationFrame(int x, int y, int size, int panelSize) {
        this.setLocation(x, y);
        this.setSize(size, size);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.panelSize = panelSize;

        cols = size / panelSize;
        rows = size / panelSize;

        initGame();

    }

    
    /* Sets the position of the window (x, y), the size of the window and the size of an individual cell of the game. (Higher size, less cells) **/
    public ApplicationFrame() {
        this(100, 100, 1000, 30);
    }

    private void initGame() {
        initComponents();
        initEvents();
        configureComponents();

        snakeSize = 5;
        this.snake = new ArrayList<>();
        for (int i = 0; i < snakeSize; i++) {
            snake.add(new Point(0, 0));
        }

        reward = new Point();
        moveReward();
        gameCells.get(0).setBackground(Color.red);

        speed[0] = 1;
        speed[1] = 0;

        if (snakeMover == null) {
            snakeMover = new SnakeMover();
            snakeMover.start();
        }
    }

    private void initComponents() {
        panel = new JPanel();
        fieldLayout = new GridLayout(rows, cols);
        gameCells = new ArrayList<>();

    }

    private void configureComponents() {
        fieldLayout.setHgap(gap);
        fieldLayout.setVgap(gap);

        panel.setBackground(Color.BLACK);
        panel.setLayout(fieldLayout);

        JPanel newPanel;
        int i = 0;
        int j = 0;
        for (j = 0; j < rows; j++) {
            for (i = 0; i < cols; i++) {
                newPanel = new JPanel();
                newPanel.setBackground(Color.WHITE);
                panel.add(newPanel);
                gameCells.add(newPanel);
            }

        }
        //System.err.println("Rows: " + i + "   Cols : " + j + " Size : " + panelSize + " Array : " + gameCells.size());

        this.getContentPane().add(panel);
        this.setVisible(true);
    }

    private void moveReward() {
        Random rand = new Random();
        reward.x = rand.nextInt(cols);
        reward.y = rand.nextInt(rows);

        drawCell(reward, Color.BLUE);
    }

    private void initEvents() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {

                System.err.println(ke.getKeyCode());

                /* Arrow keys */
                switch (ke.getKeyCode()) {

                    case 37:
                    case 65: { //Left arrow key
                        changeDirectionX(-1);
                        break;
                    }

                    case 38:
                    case 87: { //Up arrow key
                        changeDirectionY(-1);
                        break;
                    }

                    case 39:
                    case 68: { //Right arrow key
                        changeDirectionX(1);
                        break;
                    }

                    case 40:
                    case 83: { //Down arrow key
                        changeDirectionY(1);
                        break;
                    }
                    case 32: {
                        increaseSnake();
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private synchronized void changeDirectionX(int x) {

        if (snake.get(0).y != snake.get(1).y) {
            speed[0] = x;
            speed[1] = 0;
        }
    }

    private synchronized void changeDirectionY(int y) {
        if (snake.get(0).x != snake.get(1).x) {
            speed[0] = 0;
            speed[1] = y;
        }
    }

    private synchronized void moveSnake() {
        Point last = snake.get(snake.size() - 1);
        drawCell(last, Color.WHITE);

        Point newHead = new Point();

        newHead.x = snake.get(0).x + speed[0];
        newHead.y = snake.get(0).y + speed[1];

        shiftRight();

        /* If out of bounds it reapears in the other side*/
        if (newHead.x >= rows) {
            newHead.x = 0;
        } else {
            if (newHead.x < 0) {
                newHead.x = rows-1;
            }
        }

        if (newHead.y >= cols) {
            newHead.y = 0;
        } else {
            if (newHead.y < 0) {
                newHead.y = cols - 1;
            }
        }

        /* If the snake collides with itself */
        if (checkPosition(newHead)) {
            initGame();
        } else {

            if (newHead.equals(reward)) {
                increaseSnake();
                moveReward();
            }

            snake.set(0, newHead);
            drawCell(newHead, Color.ORANGE);
        }
    }

    private void drawCell(Point p, Color color) {
        int position = (int) (p.getY() * cols + p.getX());
        gameCells.get(position).setBackground(color);
    }

    private void shiftRight() {
        //make a loop to run through the array list
        for (int i = snake.size() - 1; i > 0; i--) {
            //set the last element to the value of the 2nd to last element
            snake.set(i, snake.get(i - 1));
            drawCell(snake.get(i), Color.RED);
        }
    }

    /* Return true if the snake collides with itself */
    private boolean checkPosition(Point headPos) {
        if (snake.contains(headPos)) {
            return true;
        }

        return false;
    }

    private void increaseSnake() {
        /* Increases the snake lenght by 3*/
        for (int i = 0; i < 3; i++) {
            snake.add(new Point(snake.get(snakeSize - 1)));
            snakeSize++;
        }
    }

    private class SnakeMover extends Thread {

        public boolean isActive = true;

        @Override
        public void run() {
            while (isActive) {
                try {
                    moveSnake();
                    /* Less number == higherSpeed*/
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ApplicationFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
