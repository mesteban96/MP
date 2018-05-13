/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan
 */
public class Player {

    int id;

    List<Point> snake;
    int points;
    int size;
    boolean isAlive;
    boolean isConnected;

    private Integer[] speed;

    private Color color;

    public Player(int idclient) {
        this.id = idclient;
        isAlive = false;
        isConnected = true;
        this.size = 5;
        this.snake = new ArrayList<>();
        
        for(int i = 0; i < size; i++){
            snake.add(new Point(0, 0));
        }
        speed = new Integer[2];
        speed[0] = 1;
        speed[1] = 0;
        color = new Color((float) Math.random() * 0.8f, (float) Math.random() * 0.8f, (float) Math.random() * 0.8f);
    }

    public void restart() {
        isAlive = true;
        speed[0] = 1;
        speed[1] = 0;
        points = 0;
        size = 5;
        this.snake = new ArrayList<>();
    }

    public void startSnake(int x, int y) {
        restart();
        for (int i = 0; i < size; i++) {
            snake.add(new Point(x, y));
        }
    }

    public Integer getId() {
        return this.id;
    }

    public List<Point> getSnake() {
        return this.snake;
    }

    public Integer getSpeedX() {
        return speed[0];
    }

    public Integer getSpeedY() {
        return speed[1];
    }

    public Integer[] getSpeed() {
        return this.speed;
    }

    public void setSpeedX(int s) {
        speed[0] = s;
    }

    public void setSpeedY(int s) {
        speed[1] = s;
    }

    void setAlive(boolean b) {
        this.isAlive = b;
    }

    public void disconnect() {
        this.isConnected = false;
    }

    public Color getColor() {
        return this.color;
    }

    public int increasePoints(int points) {
        this.points += points;
        return this.points;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public int size() {
        return this.snake.size();
    }

    public int getPoints() {
        return this.points;
    }

}
