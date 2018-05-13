/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Controllers;


import java.util.Observable;

/**
 *
 * @author ivan
 */
public abstract class AbstractController extends Observable {


    protected int id;
    
    public AbstractController () {
        
    }

    public abstract void move(int dirX, int dirY);
}
