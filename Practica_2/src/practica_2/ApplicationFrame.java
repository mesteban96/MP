/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ivanm
 */
public class ApplicationFrame extends JFrame {

    
    BoardPanel gamePanel;

    public ApplicationFrame(int x, int y, int size, int panelSize) {
        this.setLocation(x, y);
        this.setSize(size, size);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.gamePanel = new BoardPanel(this, size, panelSize);        
    }
    
    /* Sets the position of the window (x, y), the size of the window and the size of an individual cell of the game. (Higher size, less cells) **/
    public ApplicationFrame() {
        this(100, 100, 800, 30);
    }
    
    public void init(){
        this.gamePanel.initGame();
        initEvents();
        this.getContentPane().add(gamePanel);
        this.setVisible(true);
    };

    private void initEvents() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                /* Arrow keys */
                switch (ke.getKeyCode()) {

                    case 37:
                    case 65: { //Left arrow key
                        gamePanel.changeDirectionX(-1);
                        break;
                    }

                    case 38:
                    case 87: { //Up arrow key
                        gamePanel.changeDirectionY(-1);
                        break;
                    }

                    case 39:
                    case 68: { //Right arrow key
                        gamePanel.changeDirectionX(1);
                        break;
                    }

                    case 40:
                    case 83: { //Down arrow key
                        gamePanel.changeDirectionY(1);
                        break;
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
}
