/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 *
 * @author ivan
 */
public class InternalSnakeState extends Observable {

    private final int rows, cols;

    private ArrayList<Point> snake;

    private int size;

    private int points;

    private int[] speed = new int[2];

    private Point cellToDraw;

    private Color cellColor;

    private Point reward;
    
    private RunnableSnake snakeMover;
    
    private boolean restartGame = false;

    public InternalSnakeState(int size, int panelSize) {
        this.rows = size / panelSize;
        this.cols = rows;
        
    }
    
    public void initGame(){
        snakeMover = new RunnableSnake(this);
        restartGame();
        
        (new Thread(snakeMover) ).start();
    }

    private void restartGame() {
        
        this.restartGame = true;
        setChanged();
        notifyObservers();
        
        snakeMover.finish();
        restartSnake();
        
        snakeMover = new RunnableSnake(this);
        (new Thread(snakeMover) ).start();
        
        reward = new Point();
        moveReward();
        
    }

    public Point getCellToDraw() {
        return cellToDraw;
    }

    public Color getCellColor() {
        return cellColor;
    }

    public boolean isRestartGame() {
        return restartGame;
    }
    

    private void restartSnake() {
        points = 0;
        size = 5;
        this.snake = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            snake.add(new Point(0, 0));
        }

        speed[0] = 1;
        speed[1] = 0;
    }

    private void shiftRight() {
        //make a loop to run through the array list
        for (int i = snake.size() - 1; i > 0; i--) {
            //set the last element to the value of the 2nd to last element
            snake.set(i, snake.get(i - 1));
            drawCell(snake.get(i), Color.RED);
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
                increaseSnake();
                moveReward();
            }

            snake.set(0, newHead);
            drawCell(newHead, Color.ORANGE);
        }
    }

    /* Return true if the snake collides with a point */
    private boolean checkSnakeCollition(Point pos) {
        return snake.contains(pos);
    }

    public void drawCell(Point p, Color c) {

        this.cellToDraw = p;
        this.cellColor = c;
        this.restartGame = false;
        setChanged();
        notifyObservers();
    }

    private void increaseSnake() {
        /* Increases the snake lenght by 3*/
        for (int i = 0; i < 3; i++) {
            snake.add(new Point(snake.get(size - 1)));
            size++;
        }
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

}
