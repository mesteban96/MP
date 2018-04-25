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
public class HumanController extends AbstractController {

    public HumanController(InternalSnakeState internalSnakeState, int id) {
        super(internalSnakeState, id);
    }

    @Override
    public synchronized void move(int dirX, int dirY) {
        if (dirX != 0) {
            internalSnake.changeDirectionX(dirX, id);
        }
        if (dirY != 0) {
            internalSnake.changeDirectionY(dirY, id);
        }
    }

}
