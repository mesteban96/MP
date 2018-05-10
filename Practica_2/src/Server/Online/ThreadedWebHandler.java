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

/**
 *
 * @author ivan
 */
public class ThreadedWebHandler extends Thread implements Observer{
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
        incoming = socket;
        idclient = c;
        keepConected = true;
        internalSnakeState = internal;
        
        player = new Player(idclient);
        
        controller = new HumanController(internalSnakeState, player);
    }

    public void run() { // Redefinición de run
        internalSnakeState.addPlayer(player);
        in = null;
        try {
            in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            out = new PrintWriter(incoming.getOutputStream(), true);
            System.out.print("Cliente " + idclient + "\n");
            String line;
            while (!(line = in.readLine()).equals("") && keepConected) {
                System.out.println("Client " + idclient + " " + line);
                parseAction (line);
            }
            in.close();
            out.close();
            incoming.close();
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    private void parseAction (String message){
        String [] instruction =  message.split(";");
        switch (instruction[0]) {
            case "DIR" : {
                Integer [] speed = new Integer [2]; 
                switch (instruction[1]) {
                    case "ARRIBA" : {
                        speed[0] = 0;
                        speed[1] = -1;
                        break;
                    }
                    case "ABAJO" : {
                        speed[0] = 0;
                        speed[1] = 1;
                        break;
                    }
                    case "IZQ" : {
                        speed[0] = -1;
                        speed[1] = 0;
                        break;
                    }
                    case "DER" : {
                        speed[0] = 1;
                        speed[1] = 0;
                        break;
                    }
                    default : {
                        speed[0] = 1;
                        speed[1] = 0;
                        break;
                    }
                }
                controller.move(speed[0], speed[1]);
                break;
            }
            
            case "FIN" : {
                
                break;
            }
        }
    }

    @Override
    public void update(Observable o, Object o1) {
        
        //send draw
    }
}
