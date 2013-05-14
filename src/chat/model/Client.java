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

    /**
     * enviar solicitud al tracer
     */
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

    public void getListHostSever(String in) {
        Gson gson = new Gson();
        Host fromJson = gson.fromJson(in, Host.class);

        this.hosts = new LinkedList<>();
        this.hosts.add(fromJson);
    }

    /**
     * enviar mensaje del server
     * @param mensaje 
     */
    public void enviartoServer(String mensaje) {

        writetoServer.println(mensaje);
    }
/**
     * recibir mensaje del server
     * @param mensaje 
     */
    public String recibirtoServer() {
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
            while (true) {
                String message = readtoServer.readLine();

                if (message == null) {
                    continue;
                }

                return message;
            }
        } catch (IOException ex) {
            Logger.getLogger(Rastreador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "no hay clientes";
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

    /**
     * envia un mensaje a un par cliente
     *
     * @param mensaje
     */
    public void enviar(String mensaje) {

        write.println(mensaje);
    }

    /**
     * recibe un mensaje de un par cliente
     *
     * @return
     */
    public String recibir() {
        String mostrar = null;
        try {
            mostrar = read.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mostrar;
    }

    /**
     * Se ejecuta el serversocket esperando una solicitud del lciente
     */
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

    /**
     * cerrar todos los canales o flujos de entrada y salidas y el socket
     */
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

    /**
     * get y sets
     */
    public LinkedList<Host> getHosts() {
        return hosts;
    }

    public void setHosts(LinkedList<Host> hosts) {
        this.hosts = hosts;
    }
}
