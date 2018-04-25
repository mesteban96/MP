/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2.Controllers;

import practica_2.Model.InternalSnakeState;

/**
 *
 * @author ivan
 */
public class AutomaticController extends AbstractController {

    private AutomaticPlayer automaticPlayer;
    Thread t;

    public AutomaticController(InternalSnakeState internalSnakeState, int id) {
        super(internalSnakeState, id);

        t = new Thread(automaticPlayer);
    }

    public void start() {
        t.start();
    }

    public synchronized void move(int dirX, int dirY) {
        internalSnake.changeDirectionX(dirX, id);
        internalSnake.changeDirectionY(dirY, id);
    }

}
