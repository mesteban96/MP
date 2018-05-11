/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controllers;

import Server.Model.InternalSnakeState;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;

/**
 *
 * @author ivan
 */
public class OnlineController extends AbstractController {

    Socket socket;

    BufferedReader in;
    PrintWriter out;

    int posX, posY;
    Color colorToDraw;

    int idClient;
    int operation;

    public OnlineController(Socket s) {
        super();
        operation = 0;
        try {
            this.socket = s;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(OnlineController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        try {
      
            System.out.print("Cliente ee " + id + "\n");
            String line;
            while (!(line = in.readLine()).equals("")) {
                parseAction(line);
            }

        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void parseAction(String line) {
        String s = line;
        String[] instruction = s.split(";");
        
        switch (instruction[0]) {
            case "DRAW": {
                posX = Integer.parseInt(instruction[2]);
                posY = Integer.parseInt(instruction[3]);
                colorToDraw = new Color(Integer.parseInt(instruction[4]));
                operation = 1;
                this.setChanged();
                this.notifyObservers();
                break;
            }
            
            case "IDC" : {
                this.idClient = Integer.parseInt(instruction[1]);
                break;
            }

            case "FIN": {

                break;
            }
        }
    }

    @Override
    public void move(int dirX, int dirY) {
        String msg = "DIR;" + this.idClient + ";";
        if (dirX != 0) {
            msg += (dirX == 1) ? "DER" : "IZQ";
        } else if (dirY != 0) {
            msg += (dirY == 1) ? "ABAJO" : "ARRIBA";
        }
        out.println(msg);
    }

    /**
     *
     *
     * @return operation * 0 = No Action * 1 = Draw Cell * 2 = Update Points
     * 
     */
    
    
    public int getOperation() {
        return this.operation;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Color getColorToDraw() {
        return colorToDraw;
    }

}
