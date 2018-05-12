/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Controller;

import Server.Model.InternalSnakeState;
import Server.Model.Player;

/**
 *
 * @author ivan
 */
public abstract class AbstractController {

    InternalSnakeState internalSnake;
    protected Player player;

    public AbstractController(InternalSnakeState internalSnakeState, Player player) {
        this.internalSnake = internalSnakeState;
        this.player = player;
    }

    public abstract void move(int dirX, int dirY);

    public synchronized void changeDirectionX(int x, Player player) {
        if (player.getSnake().get(0).y != player.getSnake().get(1).y) {
            player.setSpeedX(x);
            player.setSpeedY(0);
        }
    }

    public synchronized void changeDirectionY(int y, Player player) {
        if (player.getSnake().get(0).x != player.getSnake().get(1).x) {
            player.setSpeedX(0);
            player.setSpeedY(y);
        }
    }

}
