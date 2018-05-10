/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controllers;

import Server.Model.InternalSnakeState;

/**
 *
 * @author ivan
 */
public abstract class AbstractController {

    InternalSnakeState internalSnake;
    protected int id;

    public AbstractController(InternalSnakeState internalSnakeState, int id) {
        this.internalSnake = internalSnakeState;
        this.id = id;
    }

    public abstract void move(int dirX, int dirY);
}