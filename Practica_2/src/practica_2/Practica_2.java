/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import practica_2.Controllers.AbstractController;
import practica_2.Controllers.AutomaticController;
import practica_2.Controllers.HumanController;
import practica_2.Model.InternalSnakeState;
import practica_2.GUI.ApplicationFrame;

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
        startApp(100, 0, 800, 10);
    }

    private static void startApp(int x, int y, int size, int panelSize) {
        InternalSnakeState internalState = new InternalSnakeState(size, panelSize);

        ApplicationFrame guiFrame = new ApplicationFrame(x, y, size, panelSize, internalState);
        guiFrame.init();
        AbstractController controller1 = new HumanController(internalState, internalState.addPlayer());
        guiFrame.setController(controller1);

        AutomaticController controller2 = new AutomaticController(internalState, internalState.addPlayer());
        AutomaticController controller3 = new AutomaticController(internalState, internalState.addPlayer());

        controller2.start();
        controller3.start();

        internalState.initGame();

    }

}
