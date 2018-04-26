/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2.GUI;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import practica_2.Controllers.AbstractController;
import practica_2.Model.InternalSnakeState;

/**
 *
 * @author ivan
 */
public class PuntuationFrame extends JFrame implements Observer{
    

    private InternalSnakeState internalSnakeState;

    private AbstractController controller;
    
    private List<JLabel> punctuationsTable;
    private int numberPlayers;
    private JPanel panel;
    private List<JPanel> colorsPanel;
    public PuntuationFrame(int x, int y, int numberPlayers, InternalSnakeState internalSnakeState) {
        this.setLocation(x, y);
        this.setSize(300, 50 * (numberPlayers + 1) );
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.internalSnakeState = internalSnakeState;
        punctuationsTable = new ArrayList<> ();
        this.numberPlayers = numberPlayers;
        colorsPanel = new ArrayList<> ();
        panel = new JPanel();
    }

    public void init() {
        panel.setLayout(null);
        for (int i = 0; i < this.numberPlayers; i++){
            JLabel label = new JLabel("");
            label.setLocation(60, 50 * i + 10 );
            label.setSize(200, 30);
            punctuationsTable.add(label);
            JPanel colorPanel = new JPanel();
            colorPanel.setSize(30, 30);
            colorPanel.setLocation(10, 50 * i + 10);
            colorPanel.setBackground(Color.WHITE);
            colorsPanel.add(colorPanel);
            
            panel.add(colorPanel);
            panel.add(label);
            
            System.out.println(i);
        }
        this.getContentPane().add(panel);
        this.setVisible(true);
    }

    @Override
    public void update(Observable o, Object o1) {
        if (this.internalSnakeState.getOperation() == 3) {
            int i = 0;
            for (JLabel label : punctuationsTable){
                colorsPanel.get(i).setBackground(this.internalSnakeState.getSnakeColor().get(i));
                String message = "Player " +  i + ": Points : " + this.internalSnakeState.getPoints().get(i);
                label.setText(message);
                i++;
            }
        }
    }

}
