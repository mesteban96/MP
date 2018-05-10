/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Online;

import Server.Model.InternalSnakeState;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public class Server {

    private final int puerto = 8000;
    
    private final int panelSize = 20, size = 800; 
    
    private InternalSnakeState internalSnakeState;

    public Server() {
        internalSnakeState = new InternalSnakeState(size, panelSize);
    }

    public void start() {

        ServerSocket s;
        try {
            s = new ServerSocket(puerto);

            System.out.println("Esperando conexiones en " + s.toString() + "  ...\n");
            int i = 0;

            for (;;) {
                Socket incoming = s.accept();
                
                Thread t = new ThreadedWebHandler(incoming, i, internalSnakeState);
                t.start();
                if (i == 0) {
                    internalSnakeState.initGame();
                }
                i++;
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
