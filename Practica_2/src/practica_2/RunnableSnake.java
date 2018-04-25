/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import practica_2.Model.InternalSnakeState;
import practica_2.GUI.ApplicationFrame;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramon Campayo_2
 */
public class RunnableSnake implements Runnable {

    private InternalSnakeState internalSnake;
    private boolean isAlive;
    Thread timer;
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
    
    public synchronized void finish () {
        isAlive = false;
    }
    
    public synchronized boolean isAlive(){
        return this.isAlive;
    }

    @Override
    public synchronized void run() {
        this.timer.start();
        while (isAlive) {
            try {
                internalSnake.moveSnakes();
                /* Less number == higherSpeed*/
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ApplicationFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
