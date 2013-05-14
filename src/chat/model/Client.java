/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jefferson
 */
public class Client extends Thread {

    private BufferedReader read;
    private PrintStream write;
    private BufferedReader readtoServer;
    private PrintStream writetoServer;
    private LinkedList<Host> hosts;
    private Socket socket;
    private Socket sockettoSever;
    private String ip;
    private String nick;

    public Client(String ip, String nick) {
        this.ip = ip;
        this.nick = nick;
    }

    public Client() {
    }

    public void iniciarServer() {
        socket = null;
        try {
            socket = new Socket("192.168.2.102", 1618);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            readtoServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writetoServer = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.enviartoServer(this.ip + " " + this.nick);
    }

    public String iniciar(String ip) {
        socket = null;
        try {
            socket = new Socket(ip, 4000);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            write = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Ha iniciado sesion con la ip: " + ip;
    }

    public void enviar(String mensaje) {

        write.println(mensaje);
    }

    public void getListHostSever(String in) {
        this.hosts = new LinkedList<>();
        String a[] = in.split(",");
        for (int i = 0; a.length > i; i++) {
            this.hosts.add(new Host(a[i + 1], a[i]));
        }
    }

    public String recibir() {
        String mostrar = null;
        try {
            mostrar = read.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mostrar;
    }

    public void enviartoServer(String mensaje) {

        writetoServer.println(mensaje);
    }

    public String recibirtoServer() {
        String mostrar = null;
        try {
            mostrar = readtoServer.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mostrar;
    }

    public void closeCanales() {
        try {
            read.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        write.close();
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(4000);
            while (true) {
                this.socket = serverSocket.accept();
                try {
                    write = new PrintStream(this.socket.getOutputStream());
                    read = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (socket != null) {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public LinkedList<Host> getHosts() {
        return hosts;
    }

    public void setHosts(LinkedList<Host> hosts) {
        this.hosts = hosts;
    }
    
}
