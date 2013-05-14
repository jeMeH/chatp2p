/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.main;

import chat.model.Client;
import chat.model.Rastreador;
import chat.view.FrameChat;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author jefferson
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args[0].equals("R") || args[0].equals("r")) {
                try {
                    Rastreador r = new Rastreador();
                    r.start();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                String nick = JOptionPane.showInputDialog(null, "ingrese nick");
                String ip = JOptionPane.showInputDialog(null, "ingrese ip");
                Client cliente = new Client(ip, nick);
                FrameChat frame = new FrameChat(cliente);
                frame.setBounds(10, 10, 600, 500);
                frame.setTitle("Chat p2p - " + nick);
                frame.setVisible(true);
            }
        } catch (Exception ex) {
            String nick = JOptionPane.showInputDialog(null, "ingrese nick");
            String ip = JOptionPane.showInputDialog(null, "ingrese ip");
            Client cliente = new Client(ip, nick);
            FrameChat frame = new FrameChat(cliente);
            frame.setBounds(10, 10, 600, 500);
            frame.setTitle("Chat p2p -" + nick);
            frame.setVisible(true);
        }
    }
}
