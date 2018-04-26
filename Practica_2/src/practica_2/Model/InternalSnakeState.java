/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2.Model;

import com.sun.webkit.Timer;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica_2.RunnableSnake;

/**
 *
 * @author ivan
 */
public class InternalSnakeState extends Observable {

    private final int rows, cols;

    private List<List<Point>> snakes;

    private List<Integer> size;

    private List<Integer> points;

    private List<Integer[]> speed;

    private List<Color> snakeColor;

    private Point cellToDraw;

    private Color cellColor;

    private Point reward;

    private RunnableSnake snakeMover;

    private Double time;

    private int numPlayers;

    private List<Boolean> isAlive;

    private int alivePlayers;

    /**
     * Operations : * 0 = Do nothing * 1 = Restart Game * 2 = Paint cell * 3 =
     * Get Time * 4 = End turn
     */
    private int operation = 0;

    public InternalSnakeState(int size, int panelSize) {
        this.rows = size / panelSize;
        this.cols = rows;
        this.restartPlayers();
    }

    private void restartPlayers() {
        this.numPlayers = 0;
        this.alivePlayers = 0;
        snakes = new ArrayList<>();
        this.size = new ArrayList<>();
        this.points = new ArrayList<>();
        this.speed = new ArrayList<>();
        this.snakeColor = new ArrayList<>();
        this.isAlive = new ArrayList<>();
        this.time = 0d;
    }

    public int addPlayer() {
        snakes.add(new ArrayList<>());
        points.add(0);
        size.add(5);

        int randomX = ThreadLocalRandom.current().nextInt(0, this.cols);
        int randomY = ThreadLocalRandom.current().nextInt(0, this.rows);

        for (int i = 0; i < size.get(numPlayers); i++) {
            snakes.get(numPlayers).add(new Point(randomX, randomY));
        }

        speed.add(new Integer[2]);
        speed.get(numPlayers)[0] = 1;
        speed.get(numPlayers)[1] = 0;
        Color c = new Color((float) Math.random() * 0.8f, (float) Math.random() * 0.8f, (float) Math.random() * 0.8f);

        snakeColor.add(c);
        isAlive.add(true);

        numPlayers++;
        alivePlayers++;
        return numPlayers - 1;
    }

    private void removePlayer(int id) {
        for (Point p : snakes.get(id)) {
            this.drawCell(p, Color.WHITE);
            p.x = -100;
            p.y = -100;
        }
        speed.get(id)[0] = 0;
        speed.get(id)[1] = 0;
        isAlive.set(id, false);
        alivePlayers--;
    }

    public void initGame() {
        snakeMover = new RunnableSnake(this);
        reward = new Point();
        moveReward();
        (new Thread(snakeMover)).start();
    }

    private void restartSnakes(int players) {
        for (int i = 0; i < players; i++) {
            this.addPlayer();
        }
    }

    private void restartGame() {

        snakeMover.pause();
        this.operation = 1;
        setChanged();
        notifyObservers();
        int players = numPlayers;
        restartPlayers();
        restartSnakes(players);
        numPlayers = players;
        alivePlayers = players;

        reward = new Point();
        moveReward();

        snakeMover.resume();
    }

    public Point getCellToDraw() {
        return cellToDraw;
    }

    public Color getCellColor() {
        return cellColor;
    }

    /**
     *
     * @return Operations : 0 = Do nothing 1 = Restart Game 2 = Paint cell 3 =
     * Get Time 4 = End turn
     */
    public int getOperation() {
        return operation;
    }

    private void shiftRight(int id) {
        //make a loop to run through the array list
        for (int i = snakes.get(id).size() - 1; i > 0; i--) {
            //set the last element to the value of the 2nd to last element
            snakes.get(id).set(i, snakes.get(id).get(i - 1));
            drawCell(snakes.get(id).get(i), snakeColor.get(id));
        }
    }

    public synchronized void moveSnake(int id) {
        Point last = snakes.get(id).get(snakes.get(id).size() - 1);
        drawCell(last, Color.WHITE);

        Point newHead = new Point();

        newHead.x = snakes.get(id).get(0).x + speed.get(id)[0];
        newHead.y = snakes.get(id).get(0).y + speed.get(id)[1];

        shiftRight(id);

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

        /* If the snake collides with itself or with others */
        if (checkSnakeCollition(this.snakes, newHead)) {
            if (alivePlayers <= 2) {
                restartGame();
            } else {
                removePlayer(id);
            }
        } else {
            if (newHead.equals(reward)) {
                points.set(id, points.get(id) + 1);
                increaseSnake(id);
                moveReward();
            }

            snakes.get(id).set(0, newHead);
            drawCell(newHead, Color.ORANGE);
        }

        this.operation = 4;
        setChanged();
        notifyObservers(4);
    }

    public synchronized void moveSnakes() {
        for (int i = 0; i < this.numPlayers; i++) {
            if (isAlive.get(i)) {
                moveSnake(i);
            }
        }
    }

    /* Return true if the snake collides with a point */
    public static boolean checkSnakeCollition(List<List<Point>> snakes, Point pos) {
        for (List<Point> snake : snakes) {
            if (snake.contains(pos)) {
                return true;
            }
        }
        return false;
    }

    public void drawCell(Point p, Color c) {
        this.cellToDraw = p;
        this.cellColor = c;
        this.operation = 2;
        setChanged();
        notifyObservers();
    }

    private void increaseSnake(int id) {
        /* Increases the snake lenght by 3*/
        for (int i = 0; i < 3; i++) {
            snakes.get(id).add(new Point(snakes.get(id).get(size.get(id) - 1)));
            size.set(id, size.get(id) + 1);
        }
    }

    private void moveReward() {
        do {
            Random rand = new Random();
            reward.x = rand.nextInt(cols);
            reward.y = rand.nextInt(rows);
        } while (checkSnakeCollition(this.snakes, reward));

        drawCell(reward, Color.BLUE);
    }

    public synchronized void changeDirectionX(int x, int id) {
        if (snakes.get(id).get(0).y != snakes.get(id).get(1).y) {
            speed.get(id)[0] = x;
            speed.get(id)[1] = 0;
        }
    }

    public synchronized void changeDirectionY(int y, int id) {
        if (snakes.get(id).get(0).x != snakes.get(id).get(1).x) {
            speed.get(id)[0] = 0;
            speed.get(id)[1] = y;
        }
    }

    public synchronized void increaseTime(double time) {
        this.operation = 3;
        this.time += time / 1000;
        setChanged();
        notifyObservers();
    }

    public synchronized double getTime() {

        return (double) Math.round(this.time * 1000d) / 1000d;
    }

    public synchronized void setTime(double time) {
        this.time = time;
    }

    public synchronized List<List<Point>> getSnakes() {
        return this.snakes;
    }

    public synchronized Point getReward() {
        return this.reward;
    }

    public synchronized Integer[] getSpeed(int id) {
        return speed.get(id);
    }

}
