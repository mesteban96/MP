/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controllers;

import Server.Model.InternalSnakeState;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan
 */
public class HumanController extends AbstractController {
    
    public Socket sc;
    public DataOutputStream mov;

    public HumanController(InternalSnakeState internalSnakeState, int id) {
        super(internalSnakeState, id);
        sc = new Socket(ip, 8000); /*conectar a un servidor en localhost con puerto 5000*/
    }

    @Override
    public synchronized void move(int dirX, int dirY) {
        if (dirX != 0) {
            try {
                mov = new DataOutputStream(sc.getOutputStream());
                mov.writeUTF("X=" + dirX + "id=" + id);
            } catch (IOException ex) {
                Logger.getLogger(HumanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            internalSnake.changeDirectionX(dirX, id);
        }
        if (dirY != 0) {
            try {
                mov = new DataOutputStream(sc.getOutputStream());
                mov.writeUTF("Y=" + dirY + "id=" + id);
            } catch (IOException ex) {
                Logger.getLogger(HumanController.class.getName()).log(Level.SEVERE, null, ex);
            }
            internalSnake.changeDirectionY(dirY, id);
        }
    }

}
