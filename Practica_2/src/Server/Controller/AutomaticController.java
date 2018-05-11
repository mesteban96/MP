/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import Client.Controllers.*;
import java.util.Observable;
import java.util.Observer;
import Server.Model.InternalSnakeState;
import Server.Model.Player;

/**
 *
 * @author ivan
 */
public class AutomaticController extends AbstractController implements Observer {

    private AutomaticPlayer automaticPlayer;
    Thread t;

    public AutomaticController(InternalSnakeState internalSnakeState, Player player) {
        super(internalSnakeState, player);

        automaticPlayer = new AutomaticPlayer(this);
    }

    public void start() {
        (new Thread(this.automaticPlayer)).start();
    }

    @Override
    public synchronized void move(int dirX, int dirY) {
        if (dirX != 0) {
            this.changeDirectionX(dirX, player);
        } else {
            this.changeDirectionY(dirY, player);
        }
    }

    @Override
    public void update(Observable o, Object o1) {
        if (o1 instanceof Integer) {
            Integer op = (Integer) o1;
            if (op != null && op == 4) {
                automaticPlayer.resume();
            }
        }
    }
}
