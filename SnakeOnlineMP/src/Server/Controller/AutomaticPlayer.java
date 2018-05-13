/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import java.awt.Point;
import Server.Model.InternalSnakeState;
import Server.Model.Player;
import java.util.Collection;

/**
 *
 * @author ivan
 */
public class AutomaticPlayer implements Runnable {

    private boolean running = true;
    private boolean paused = false;
    private volatile Object pauseLock = new Object();

    private AbstractController automaticContoller;

    public AutomaticPlayer(AutomaticController automatic) {
        this.automaticContoller = automatic;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (pauseLock) {
                if (!running) { // may have changed while waiting to
                    // synchronize on pauseLock
                    break;
                }
                if (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) { // running might have changed since we paused
                        break;
                    }
                }
            }
            this.movementAlgorithm();
        }
    }

    public void stop() {
        running = false;
        // you might also want to interrupt() the Thread that is 
        // running this Runnable, too, or perhaps call:
        resume();
        // to unblock
    }

    public void pause() {
        // you may want to throw an IllegalStateException if !running
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }

    private synchronized void movementAlgorithm() {

        Collection<Player> players = automaticContoller.internalSnake.getPlayers();
        Point reward = automaticContoller.internalSnake.getReward();
        Player player = automaticContoller.player;
        Integer[] speed = player.getSpeed();

        speed = this.pointToReward(reward, player.getSnake().get(0), speed);

        speed = correctMovement(players, speed, player.getSnake().get(0));

        this.automaticContoller.move(speed[0], speed[1]);
        this.pause();

    }

    private Integer[] correctMovement(Collection<Player> players, Integer[] speed, Point head) {
        Point newHead = new Point();
        boolean collides;
        int paso = 0;
        do {
            newHead.x = head.x + speed[0];
            newHead.y = head.y + speed[1];
            if (InternalSnakeState.checkSnakeCollition(players, newHead)) {
                if (paso % 2 == 0) {
                    speed[0] = paso - 1;
                    speed[1] = 0;
                } else {
                    speed[0] = 0;
                    speed[1] = paso - 2;
                }
                collides = true;
            } else {
                collides = false;
            }
            paso++;
        } while (collides && paso < 4);

        return speed;
    }

    private Integer[] pointToReward(Point reward, Point head, Integer[] speed) {
        /* No puede cambiar la speed[x], solo a 0*/
        if (speed[0] != 0) {
            if (reward.y != head.y) {
                speed[1] = (reward.y < head.y) ? -1 : 1;
                speed[0] = 0;
            }
        } else {

            if (reward.x != head.x) {
                speed[0] = (reward.x < head.x) ? -1 : 1;
                speed[1] = 0;
            }
        }
        return speed;
    }

}
