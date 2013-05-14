/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.model;

/**
 *
 * @author jefferson
 */
public class Host {
    
    private String nick;
    private String ip;

    public Host(String nick, String ip) {
        this.nick = nick;
        this.ip = ip;
    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "nick=" + nick + ", ip=" + ip;
    }
    
    
    
    
    
}
