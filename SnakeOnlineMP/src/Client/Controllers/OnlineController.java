/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controllers;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    int points;
    int idOriginComm;

    boolean persona;

    public OnlineController(Socket s, boolean persona) {
        super();
        operation = 0;
        this.persona = persona;
        try {
            this.socket = s;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException ex) {
            System.err.println(ex);
            disconnect();
        }
    }

    public void start() {
        try {

            String line;
            while (!(line = in.readLine()).equals("")) {
                parseAction(line);
            }

        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            disconnect();

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

            case "IDC": {
                this.idClient = Integer.parseInt(instruction[1]);
                String type = persona ? "PER" : "ORD";
                out.println("PLY;" + type);
                break;
            }

            case "PTS": {
                this.idOriginComm = Integer.parseInt(instruction[1]);
                this.points = Integer.parseInt(instruction[2]);
                this.operation = 2;
                this.setChanged();
                this.notifyObservers();
                break;
            }

            case "FIN": {

                break;
            }
        }
    }

    @Override
    public void move(int dirX, int dirY) {
        if (persona) {
            String msg = "DIR;" + this.idClient + ";";
            if (dirX != 0) {
                msg += (dirX == 1) ? "DER" : "IZQ";
            } else if (dirY != 0) {
                msg += (dirY == 1) ? "ABAJO" : "ARRIBA";
            }
            out.println(msg);
        }
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

    public int getIdClient() {
        return idClient;
    }

    public int getPoints() {
        return points;
    }

    public int getIdOriginComm() {
        return idOriginComm;
    }

    public void disconnect() {
        try {
            out.println("FIN;" + idClient);
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(OnlineController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.exit(0);
        }
    }

}
