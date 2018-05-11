/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Online;

import Server.Controller.AbstractController;
import Server.Controller.HumanController;
import Server.Model.InternalSnakeState;
import Server.Model.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public class ThreadedWebHandler extends Thread implements Observer {
// Atributos del handler

    private Socket incoming; // Socket con el cliente
    private int idclient; // Id para el cliente

    private BufferedReader in;
    private PrintWriter out;

    private Player player;

    AbstractController controller;

    private boolean keepConected;

    private InternalSnakeState internalSnakeState;
    // Constructor recibe atributos

    public ThreadedWebHandler(Socket socket, int c, InternalSnakeState internal) {
        try {
            incoming = socket;
            idclient = c;
            keepConected = true;
            internalSnakeState = internal;

            player = new Player(idclient);
            internalSnakeState.addPlayer(player);

            controller = new HumanController(internalSnakeState, player);
            internalSnakeState.addObserver(this);

            in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            out = new PrintWriter(incoming.getOutputStream(), true);

            out.println("IDC;" + idclient);
        } catch (IOException ex) {
            Logger.getLogger(ThreadedWebHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while (!(line = in.readLine()).equals("") || keepConected) {
                parseAction(line);
            }

        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                //player.disconnect();
                in.close();
                out.close();
                incoming.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void parseAction(String message) {
        String s = message;
        String[] instruction = s.split(";");

        switch (instruction[0]) {
            case "DIR": {
                Integer[] speed = new Integer[2];
                int id = Integer.parseInt(instruction[1]);
                
                if (player.getId() == id) {
                    switch (instruction[2]) {
                        case "ARRIBA": {
                            speed[0] = 0;
                            speed[1] = -1;
                            break;
                        }
                        case "ABAJO": {
                            speed[0] = 0;
                            speed[1] = 1;
                            break;
                        }
                        case "IZQ": {
                            speed[0] = -1;
                            speed[1] = 0;
                            break;
                        }
                        case "DER": {
                            speed[0] = 1;
                            speed[1] = 0;
                            break;
                        }
                        default: {
                            speed[0] = 1;
                            speed[1] = 0;
                            break;
                        }
                    }
                    controller.move(speed[0], speed[1]);
                }
                break;
            }

            case "FIN": {

                break;
            }
        }
    }

    @Override
    public void update(Observable o, Object o1) {
        InternalSnakeState internalSnake = (InternalSnakeState) o;
        int op = internalSnake.getOperation();

        String msg;

        if (op == 2) {
            /* Send paint a cell */
            int id = (int) o1;

            msg = "DRAW;" + id + ";" + internalSnake.getCellToDraw().x + ";" + internalSnake.getCellToDraw().y + ";" + internalSnake.getCellColor().getRGB();
            this.sendMessage(msg);
        }

        if (op == 5) {
            /* Update punctuation */
            Player p = (Player) o1;
            msg = "PTS;" + player.getId() + ";" + player.getPoints();
            this.sendMessage(msg);
        }

    }

    private void sendMessage(String msg) {
        this.out.println(msg);
    }
}
