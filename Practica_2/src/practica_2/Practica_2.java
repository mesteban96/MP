/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import java.util.ArrayList;
import java.util.List;
import Client.Controllers.AbstractController;
import Client.Controllers.AutomaticController;
import Client.Controllers.HumanController;
import Server.Model.InternalSnakeState;
import Client.GUI.ApplicationFrame;
import Client.GUI.PuntuationFrame;

/**
 *
 * @author ivanm
 */
public class Practica_2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        startApp(100, 0, 800, 20, 5);
    }

    private static void startApp(int x, int y, int size, int panelSize, int automaticPlayers) {
        InternalSnakeState internalState = new InternalSnakeState(size, panelSize);
        
        PuntuationFrame pointsFrame = new PuntuationFrame(x + size + 50, y, automaticPlayers+1, internalState);
        internalState.addObserver(pointsFrame);
        pointsFrame.init();

        ApplicationFrame guiFrame = new ApplicationFrame(x, y, size, panelSize, internalState);
        guiFrame.init();
        AbstractController controller1 = new HumanController(internalState, internalState.addPlayer());
        guiFrame.setController(controller1);

        
        List <AutomaticController> automaticControllers = new ArrayList <> ();
        for (int i = 0; i < automaticPlayers; i++) {
            AutomaticController controller = new AutomaticController(internalState, internalState.addPlayer());
            automaticControllers.add(controller);
            internalState.addObserver(controller);
        }
    
        internalState.initGame();
        
        for (AutomaticController controller : automaticControllers){
            controller.start();
        }

    }

}
