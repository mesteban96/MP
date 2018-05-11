/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.GUI;

import Client.Controllers.OnlineController;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import Server.Model.InternalSnakeState;

/**
 *
 * @author Ramon Campayo_2
 */
public class BoardPanel extends JPanel implements Observer {

    private GridLayout fieldLayout;

    private int cols, rows;

    private int panelSize;
    private final int gap = 1;

    private Observable observated;

    private ArrayList<JPanel> gameCells;

    ApplicationFrame parentFrame;

    BoardPanel(ApplicationFrame parent, int size, int panelSize, Observable observated) {
        this.parentFrame = parent;
        this.panelSize = panelSize;
        this.rows = size / panelSize;
        this.cols = rows;
        this.observated = observated;
    }

    public void initGame() {
        initComponents();
        configureComponents();

    }

    private void initComponents() {
        fieldLayout = new GridLayout(rows, cols);
        gameCells = new ArrayList<>();

    }

    private void configureComponents() {
        fieldLayout.setHgap(gap);
        fieldLayout.setVgap(gap);

        this.setBackground(Color.BLACK);
        this.setLayout(fieldLayout);

        JPanel newPanel;
        int i = 0;
        int j = 0;
        for (j = 0; j < rows; j++) {
            for (i = 0; i < cols; i++) {
                newPanel = new JPanel();
                newPanel.setBackground(Color.WHITE);
                this.add(newPanel);
                gameCells.add(newPanel);
            }
        }
        //System.err.println("Rows: " + i + "   Cols : " + j + " Size : " + panelSize + " Array : " + gameCells.size());
    }

    private void startBoard() {

    }

    private void drawCell(Point p, Color color) {
        int position = (int) (p.getY() * cols + p.getX());
        if (position > 0) {
            gameCells.get(position).setBackground(color);
        }
    }

    private void restartGame() {
        repaintBoard();

    }

    private void repaintBoard() {
        gameCells.forEach((cell) -> {
            cell.setBackground(Color.white);
        });
    }

    @Override
    public void update(Observable arg0, Object arg1) {

        if (arg0 instanceof OnlineController) {
            OnlineController onlineController = (OnlineController) arg0;
            if (onlineController.getOperation() == 1) {
                this.drawCell(new Point(onlineController.getPosX(), onlineController.getPosY()), onlineController.getColorToDraw());
            }
        }

    }

}
