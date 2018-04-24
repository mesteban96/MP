/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

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
    
    private static void startApp(){
        startApp(100, 0, 500, 30);
    }
    private static void startApp(int x, int y, int size, int panelSize){
        InternalSnakeState internalState = new InternalSnakeState(size, panelSize);
        ApplicationFrame guiFrame = new ApplicationFrame(x, y, size, panelSize, internalState);
        ApplicationFrame guiFrame2 = new ApplicationFrame(x + size + 50, y, size, panelSize, internalState);
        
        ApplicationFrame guiFrame3 = new ApplicationFrame(x, y + size + 50, size, panelSize, internalState);
        ApplicationFrame guiFrame4 = new ApplicationFrame(x + size + 50, y + size + 50, size, panelSize, internalState);
        guiFrame.init();
        guiFrame2.init();
        guiFrame3.init();
        guiFrame4.init();
        
        internalState.initGame();
        
        
    }
    
}
