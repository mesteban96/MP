/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_2;

import Client.Online.ClientOnline;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/*import practica_2.Controllers.AbstractController;
import practica_2.Controllers.AutomaticController;
import practica_2.Controllers.HumanController;
import practica_2.Model.InternalSnakeState;
import practica_2.GUI.ApplicationFrame;
import practica_2.GUI.PuntuationFrame;*/

/**
 *
 * @author ivanm
 */
public class Practica_2_menu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        String ip = (String) JOptionPane.showInputDialog("Introduzca la IP a la que se quiera conectar", "127.0.0.1");

        try {
            Socket s = new Socket(ip, 8000);
            ClientOnline client = new ClientOnline(100, 0, 800, 20, s);
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al intentar conectar con el servidor " + ip + " en el puerto 8000",
                    "Error de conexion",
                    JOptionPane.ERROR_MESSAGE);
        }

        System.exit(0);

    }

}
