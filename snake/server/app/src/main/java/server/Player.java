package server;

import java.net.InetAddress;

import javax.websocket.Session;

public class Player {
    private final InetAddress ip; // ip adress identifies a player
    private final Session session;
    private String name;
    private Snake snake;
    private int score;

    public Player(Session session, InetAddress ip) {
        this.session = session;
        this.ip = ip;
    }

    public int getScore() {
        return snake.getLength();
    }

    public InetAddress getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public Session getSession() {
        return session;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player [ip=" + ip + ", name=" + name + "]";
    }

}
