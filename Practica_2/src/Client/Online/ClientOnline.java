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
import javax.swing.JOptionPane;

/**
 *
 * @author ivan
 */
public class ClientOnline {

    public ClientOnline(int x, int y, int size, int panelSize, Socket s) {

        String seleccion = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione opcion",
                "Elija el modo de juego:",
                JOptionPane.QUESTION_MESSAGE,
                null, // null para icono defecto
                new Object[]{"PERSONA", "ORDENADOR"},
                "PERSONA");
        
        boolean persona = "PERSONA".equals(seleccion);

        OnlineController controller = new OnlineController(s, persona);
        /*PuntuationFrame pointsFrame = new PuntuationFrame(x + size + 50, y, );
        controller.addObserver(pointsFrame);
        pointsFrame.init();*/
        PuntuationFrame puntuationFrame = new PuntuationFrame(x + size + 10, y, controller);
        ApplicationFrame guiFrame = new ApplicationFrame(x, y, size, panelSize, controller);
        puntuationFrame.init();
        guiFrame.init();

        controller.start();
    }
}
