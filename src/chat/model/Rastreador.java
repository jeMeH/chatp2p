/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.model;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author atlas
 */
public class Rastreador implements Runnable{

    private ServerSocket serverSocket;
    private LinkedList<Host> clients;
    private BufferedReader readerStream = null;
    private PrintStream write;
    private Thread thread;
    private Thread threadList;

    public Rastreador() throws IOException {
        serverSocket = new ServerSocket(1618);
        this.clients = new LinkedList<>();
    }

    public void addHost(Host h) {
        this.clients.add(h);
        System.out.println(h.toString());
        this.enviarListHost();
    }

    public LinkedList<Host> getListClientes() {
        return this.clients;
    }

    @Override
    public void run() {
        try {
            while (thread != null) {
                Socket clientSocket = serverSocket.accept();
                readerStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                write = new PrintStream(clientSocket.getOutputStream());
                //si la ip esta contenida en la lista se envia la lista de usuarios
                for (Host h : this.clients) {
                    Gson gson = new Gson();
                    String g = "";
                    if (clientSocket.getLocalAddress().getHostAddress().equals(h.getIp())) {
                        g = gson.toJson(h);
                        write.print(g);
                    }
                }

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
    
    public void start (){
        if (this.thread == null){
            this.thread = new Thread(this);
        }
        if (this.threadList == null){
            this.threadList = new Thread(this);
        }
        if(!this.thread.isAlive() || this.threadList.isAlive()){
            this.start();
        }
        this.run();
    }

    public void enviarListHost() {
        int i = 0;
        String g = new String();
        Gson gson = new Gson();
        for (Host h : this.clients) {
            g = gson.toJson(h);
            this.clientsWrite.get(i).print(g);
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
