/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramon Campayo_2
 */
public class RunnableSnake implements Runnable {

    private InternalSnakeState internalSnake;
    private volatile boolean isAlive;
    Thread timer;

    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    public RunnableSnake(InternalSnakeState snake) {
        internalSnake = snake;
        isAlive = true;

        timer = new Thread(() -> {
            try {
                this.internalSnake.setTime(0);
                while (true) {
                    this.internalSnake.increaseTime(100);
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(InternalSnakeState.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public synchronized void finish() {
        isAlive = false;
    }

    public synchronized boolean isAlive() {
        return this.isAlive;
    }

    @Override
    public synchronized void run() {
        this.timer.start();
        while (isAlive) {

            synchronized (pauseLock) {
                if (!isAlive) { // may have changed while waiting to
                    // synchronize on pauseLock
                    break;
                }
                if (paused) {
                    try {
                        pauseLock.wait(); // will cause this Thread to block until 
                        // another thread calls pauseLock.notifyAll()
                        // Note that calling wait() will 
                        // relinquish the synchronized lock that this 
                        // thread holds on pauseLock so another thread
                        // can acquire the lock to call notifyAll()
                        // (link with explanation below this code)
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!isAlive) { // running might have changed since we paused
                        break;
                    }
                } 
            }
            try {
                internalSnake.moveSnakes();
                /* Less number == higherSpeed*/
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public void stop() {
        isAlive = false;
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

}
