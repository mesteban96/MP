/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Model;

import com.sun.webkit.Timer;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
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

    private Point cellToDraw;

    private Color cellColor;

    private Point reward;

    private RunnableSnake snakeMover;

    private Double time;

    private int numPlayers;

    private List<Boolean> isAlive;

    private int alivePlayers;

    private Map<Integer, Player> players;

    /**
     * Operations : * 0 = Do nothing * 1 = Restart Game * 2 = Paint cell * 3 =
     * Get Time * 4 = End turn * 5 = Change Points
     */
    private int operation = 0;

    public InternalSnakeState(int size, int panelSize) {
        this.rows = size / panelSize;
        this.cols = rows;
        players = new ConcurrentHashMap<>();
        this.restartPlayers();
    }

    private void restartPlayers() {
        this.numPlayers = 0;
        this.alivePlayers = 0;
        List<Player> toRemove = new ArrayList<>();

        for (Player player : players.values()) {
            if (player.isConnected) {
                player.restart();
            } else {
                players.remove(player.getId());
            }
        }

        /*
        if (!toRemove.isEmpty()) {
            for (Player p: toRemove) {
                players.remove(p.getId());
            }
        }
         */
        this.time = 0d;
    }

    public void addPlayer(Player player) {

        players.put(player.getId(), player);

        int randomX = ThreadLocalRandom.current().nextInt(0, this.cols);
        int randomY = ThreadLocalRandom.current().nextInt(0, this.rows);

        player.startSnake(randomX, randomY);

        numPlayers++;
        alivePlayers++;

    }

    private void removePlayer(Player player) {

        for (Point p : player.getSnake()) {
            this.drawCell(p, Color.WHITE, player.id);
            p.x = -100;
            p.y = -100;
        }
        player.setSpeedX(0);
        player.setSpeedY(0);
        player.setAlive(false);
        player.disconnect();
        alivePlayers--;
    }

    public void initGame() {
        snakeMover = new RunnableSnake(this);
        (new Thread(snakeMover)).start();
        reward = new Point();
        moveReward();
    }

    private void restartSnakes() {
        for (Player player : this.players.values()) {
            if (player.isConnected) {
                player.setAlive(true);
                this.addPlayer(player);
            }
        }
    }

    private void restartGame() {

        snakeMover.pause();
        this.operation = 1;
        setChanged();
        notifyObservers();
        int nplayers = numPlayers;
        restartPlayers();
        restartSnakes();
        numPlayers = nplayers;
        alivePlayers = nplayers;

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

    private void shiftRight(Player player) {
        //make a loop to run through the array list
        for (int i = player.size() - 1; i > 0; i--) {
            //set the last element to the value of the 2nd to last element
            player.getSnake().set(i, player.getSnake().get(i - 1));
        }
    }

    public synchronized void moveSnake(Player player) {
        Point last = player.getSnake().get(player.size() - 1);

        drawCell(last, Color.WHITE, player.getId());

        Point newHead = new Point();

        /* Paint the last cell in color */
        drawCell(player.getSnake().get(0), player.getColor(), player.getId());

        

        newHead.x = player.getSnake().get(0).x + player.getSpeedX();
        newHead.y = player.getSnake().get(0).y + player.getSpeedY();

        shiftRight(player);

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
        if (checkSnakeCollition(players.values(), newHead)) {
            if (alivePlayers <= 2) {
                restartGame();
            } else {
                removePlayer(player);
            }
        } else {
            if (newHead.equals(reward)) {
                player.increasePoints(5);
                this.operation = 5;
                setChanged();
                notifyObservers(player);
                increaseSnake(player);
                moveReward();
            }

            player.getSnake().set(0, newHead);
            drawCell(newHead, Color.ORANGE, player.id);
        }

        this.operation = 4;
        setChanged();
        notifyObservers(4);
    }

    public synchronized void moveSnakes() {
        for (Player player : players.values()) {
            if (player.isAlive() && player.isConnected()) {
                moveSnake(player);
            }
        }
    }

    /* Return true if the snake collides with a point */
    public static boolean checkSnakeCollition(Collection<Player> players, Point pos) {
        for (Player player : players) {
            if (player.getSnake().contains(pos)) {
                return true;
            }
        }
        return false;
    }

    public void drawCell(Point p, Color c, int id) {
        this.cellToDraw = p;
        this.cellColor = c;
        this.operation = 2;
        setChanged();
        notifyObservers(id);
    }

    private void increaseSnake(Player player) {
        /* Increases the snake lenght by 3*/
        for (int i = 0; i < 3; i++) {
            player.getSnake().add(new Point(player.getSnake().get(player.size() - 1)));
        }
    }

    private void moveReward() {
        do {
            Random rand = new Random();
            reward.x = rand.nextInt(cols);
            reward.y = rand.nextInt(rows);
        } while (checkSnakeCollition(players.values(), reward));

        drawCell(reward, Color.BLUE, -1);
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

    public synchronized Point getReward() {
        return this.reward;
    }
    
    public Collection<Player> getPlayers () {
        return this.players.values();
    }

}
