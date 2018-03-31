/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Ramon Campayo_2
 */
public class BoardPanel extends JPanel {
    
    private GridLayout fieldLayout;
    
    private int cols, rows;
    
    private int panelSize;
    private final int gap = 1;
    
    private int[] speed = new int[2];
    private int snakeSize;
    
    private RunnableSnake snakeMover;
    
    private ArrayList<Point> snake;
    
    private ArrayList<JPanel> gameCells;
    
    private Point reward;
    
    private int points;
    
    ApplicationFrame parentFrame;
    
    BoardPanel(ApplicationFrame parent, int size, int panelSize) {
        this.parentFrame = parent;
        this.panelSize = panelSize;
        this.rows = size / panelSize;
        this.cols = rows;
    }
    
    public void initGame() {
        initComponents();
        configureComponents();
        
        
        restartSnake();
        
        reward = new Point();
        moveReward();
        
        speed[0] = 1;
        speed[1] = 0;
        
        (new Thread(snakeMover) ).start();
    }
    
    private void initComponents() {
        fieldLayout = new GridLayout(rows, cols);
        gameCells = new ArrayList<>();
        snakeMover = new RunnableSnake(this);
    }
    
    private void configureComponents() {
        fieldLayout.setHgap(gap);
        fieldLayout.setVgap(gap);
        
        this.setBackground(Color.BLACK);
        this.setLayout(fieldLayout);
        
        JPanel newPanel;
        int i = 0;
        int j = 0;
        for (j = 0; j < rows; j++) {
            for (i = 0; i < cols; i++) {
                newPanel = new JPanel();
                newPanel.setBackground(Color.WHITE);
                this.add(newPanel);
                gameCells.add(newPanel);
            }
            
        }
        //System.err.println("Rows: " + i + "   Cols : " + j + " Size : " + panelSize + " Array : " + gameCells.size());
    }
    
    private void startBoard() {
        
    }
    
    private void moveReward() {
        
        do {
            Random rand = new Random();
            reward.x = rand.nextInt(cols);
            reward.y = rand.nextInt(rows);
        } while (checkSnakeCollition(reward));
        
        drawCell(reward, Color.BLUE);
    }
    
    public synchronized void changeDirectionX(int x) {
        
        if (snake.get(0).y != snake.get(1).y) {
            speed[0] = x;
            speed[1] = 0;
        }
    }
    
    public synchronized void changeDirectionY(int y) {
        if (snake.get(0).x != snake.get(1).x) {
            speed[0] = 0;
            speed[1] = y;
        }
    }
    
    public synchronized void moveSnake() {
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
                newHead.x = rows - 1;
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
        if (checkSnakeCollition(newHead)) {
            restartGame();
        } else {
            if (newHead.equals(reward)) {
                points++;
                parentFrame.setTitle("Puntos: " + points);
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

    /* Return true if the snake collides with a point */
    private boolean checkSnakeCollition(Point pos) {
        return snake.contains(pos);
    }
    
    private void increaseSnake() {
        /* Increases the snake lenght by 3*/
        for (int i = 0; i < 3; i++) {
            snake.add(new Point(snake.get(snakeSize - 1)));
            snakeSize++;
        }
    }
    
    private void restartGame() {
        
        snakeMover.finish();
        repaintBoard();
        restartSnake();
        moveReward();
        speed[0] = 1;
        speed[1] = 0;
        
        snakeMover = new RunnableSnake(this);
        (new Thread(snakeMover) ).start();
    }
    
    private void restartSnake() {
        points = 0;
        parentFrame.setTitle("Puntos: " + points);
        snakeSize = 5;
        this.snake = new ArrayList<>();
        for (int i = 0; i < snakeSize; i++) {
            snake.add(new Point(0, 0));
        }
        gameCells.get(0).setBackground(Color.white);
    }
    
    private void repaintBoard() {
        gameCells.forEach((cell) -> {
            cell.setBackground(Color.white);
        });
    }
    

}
