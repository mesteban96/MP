/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import Client.Controllers.*;
import Server.Model.InternalSnakeState;
import Server.Model.Player;
import Server.Online.ThreadedWebHandler;
import java.util.Observable;

/**
 *
 * @author ivan
 */
public class HumanController extends AbstractController {

    public HumanController(InternalSnakeState internalSnakeState, Player player) {
        super(internalSnakeState, player);
    }

    @Override
    public synchronized void move(int dirX, int dirY) {
        if (dirX != 0) {
            this.changeDirectionX(dirX, player);
        } else {
            if (dirY != 0) {
                this.changeDirectionY(dirY, player);
            }
        }
    }

}
