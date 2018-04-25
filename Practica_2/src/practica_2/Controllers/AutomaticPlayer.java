/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2.Controllers;

/**
 *
 * @author ivan
 */
public class AutomaticPlayer implements Runnable {
    
    private boolean running = true;
    private boolean paused = false;
    private volatile Object pauseLock = new Object();
    
    private AutomaticController automaticContoller;
    
    public AutomaticPlayer (AutomaticController automatic){
        this.automaticContoller = automatic;
    }

    @Override
    public void run () {
        while (running){
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
    
    private void movementAlgorithm () {
        
        /* Algorithm */
        this.automaticContoller.move(1, 0);
};
    
}
