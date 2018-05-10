/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Online;

import Client.Controllers.OnlineController;
import Client.GUI.ApplicationFrame;
import Client.GUI.PuntuationFrame;
import java.net.Socket;

/**
 *
 * @author ivan
 */
public class ClientOnline {
    
    
    
    public ClientOnline(int x, int y, int size, int panelSize, Socket s) {
        
        OnlineController controller = new OnlineController(s);
        /*PuntuationFrame pointsFrame = new PuntuationFrame(x + size + 50, y, );
        controller.addObserver(pointsFrame);
        pointsFrame.init();*/
        
        ApplicationFrame guiFrame = new ApplicationFrame(x, y, size, panelSize, controller);
        guiFrame.init();
        controller.start();
    }
}
