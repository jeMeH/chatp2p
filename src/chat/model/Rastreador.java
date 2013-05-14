/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author atlas
 */
public class Rastreador extends Thread {

    private ServerSocket serverSocket;
    private LinkedList<Host> clients;
    BufferedReader readerStream = null;

    public Rastreador() throws IOException {
        serverSocket = new ServerSocket(1618);
        this.clients = new LinkedList<>();
    }

    public void addHost(Host h) {

        this.clients.add(h);
        System.out.println(h.toString());
        this.start();
       

    }

    public LinkedList<Host> getListClientes() {
        return this.clients;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                readerStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                this.iniciar(clientSocket);
            }
        } catch (IOException ex) {
            Logger.getLogger(Rastreador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Rastreador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void iniciar(Socket clientSocket) {
        try {
            while (true) {
                String message = readerStream.readLine();
                if (message == null) {
                    break;
                }
                String a[] = message.split("\\ ");
                this.addHost(new Host(a[1], a[0]));
                break;
            }
        } catch (IOException ex) {
            Logger.getLogger(Rastreador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {


            if (readerStream != null) {
                try {
                    readerStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Rastreador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Rastreador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
