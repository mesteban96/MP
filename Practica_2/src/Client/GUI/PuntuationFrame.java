/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

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
import Client.Controllers.AbstractController;
import Client.Controllers.OnlineController;
import Server.Model.InternalSnakeState;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ivan
 */
public class PuntuationFrame extends JFrame implements Observer {

    private AbstractController controller;

    private int numberPlayers;
    private JPanel panel;
    private List<JPanel> colorsPanel;

    private Map<Integer, JLabel> pointsMap;

    Observable observated;

    public PuntuationFrame(int x, int y, Observable observated) {
        this.setLocation(x, y);
        this.setSize(300, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.observated = observated;
        this.numberPlayers = 0;
        colorsPanel = new ArrayList<>();
        panel = new JPanel();

        pointsMap = new HashMap<>();
    }

    public void init() {

        observated.addObserver(this);

        panel.setLayout(null);
        for (int i = 0; i < this.numberPlayers; i++) {

            System.out.println(i);
        }
        this.getContentPane().add(panel);
        this.setVisible(true);
    }

    private void addPlayer(int id, int points, int pos, String aditionalInfo) {

        JLabel label = new JLabel(aditionalInfo + " ID :" + id + " Points: " + points);
        label.setLocation(60, 50 * numberPlayers + 10);
        label.setSize(200, 30);
        
        pointsMap.put(id, label);
        JPanel colorPanel = new JPanel();
        colorPanel.setSize(30, 30);
        colorPanel.setLocation(10, 50 * numberPlayers + 10);
        colorPanel.setBackground(Color.WHITE);
        colorsPanel.add(colorPanel);

        panel.add(colorPanel);
        panel.add(label);

        numberPlayers++;

    }

    @Override
    public void update(Observable o, Object o1) {
        
        
        if (o instanceof OnlineController) {
            OnlineController controller = (OnlineController) o;
            if (controller.getOperation() == 2) {
                String aditionalInfo = "";
                int pos = numberPlayers + 1;
                
                if (controller.getIdOriginComm() == controller.getIdClient()) {
                    aditionalInfo = "Your points: ";
                    pos = 0;
                }

                if (!pointsMap.containsKey(controller.getIdOriginComm())) {
                    addPlayer(controller.getIdOriginComm(), controller.getPoints(), pos, aditionalInfo);
                } else {
                    pointsMap.get(controller.getIdOriginComm()).setText(aditionalInfo + " ID :" + controller.getIdOriginComm() + " Points: " + controller.getPoints());
                }
            }
        }
    }

}
