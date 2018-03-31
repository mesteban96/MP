/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramon Campayo_2
 */
public class RunnableSnake implements Runnable {

    private BoardPanel gamePanel;
    private boolean isAlive;

    public RunnableSnake(BoardPanel panel) {
        gamePanel = panel;
        isAlive = true;
    }
    
    public synchronized void finish () {
        isAlive = false;
    }

    @Override
    public synchronized void run() {
        while (isAlive) {
            try {
                gamePanel.moveSnake();
                /* Less number == higherSpeed*/
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ApplicationFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
