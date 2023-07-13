package server;

import java.net.InetAddress;

import javax.websocket.Session;

public class Player {
    private InetAddress ip;
    private String name;
    private Session session;
    private Snake snake;


    public Player(Session session, InetAddress ip) {
        this.session = session;
        this.ip = ip;
    }
}
