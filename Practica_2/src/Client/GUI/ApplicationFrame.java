/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import Client.Controllers.AbstractController;
import Client.Controllers.OnlineController;

/**
 *
 * @author ivanm
 */
public class ApplicationFrame extends JFrame {

    BoardPanel gamePanel;

    private AbstractController controller;

    public ApplicationFrame(int x, int y, int size, int panelSize, OnlineController controller) {
        this.setLocation(x, y);
        this.setSize(size, size);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gamePanel = new BoardPanel(this, size, panelSize, controller);
        this.controller = controller;
        controller.addObserver(gamePanel);
    }

    public void setController(AbstractController controller) {
        this.controller = controller;
    }

    public void init() {
        this.gamePanel.initGame();
        initEvents();
        this.getContentPane().add(gamePanel);
        this.setVisible(true);
    }

    ;

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
                        controller.move(-1, 0);
                        break;
                    }

                    case 38:
                    case 87: { //Up arrow key
                        controller.move(0, -1);
                        break;
                    }

                    case 39:
                    case 68: { //Right arrow key
                        controller.move(1, 0);
                        break;
                    }

                    case 40:
                    case 83: { //Down arrow key
                        controller.move(0, 1);
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
