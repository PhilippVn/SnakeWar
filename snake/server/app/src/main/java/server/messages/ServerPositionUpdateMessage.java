package server.messages;

import com.google.gson.Gson;
import java.util.List;
import java.util.Map;

public class ServerPositionUpdateMessage {
    private String messageCode;
    private Map<String, List<server.Snake.Segment>> clients;
    private List<server.Apple> apple;
    private boolean gameOver;
    private String timeStamp;

    public ServerPositionUpdateMessage() {
        this.messageCode = "position-update";
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public Map<String, List<server.Snake.Segment>> getClients() {
        return clients;
    }

    public void setClients(Map<String, List<server.Snake.Segment>> clients) {
        this.clients = clients;
    }

    public List<server.Apple> getApple() {
        return apple;
    }

    public void setApple(List<server.Apple> apple) {
        this.apple = apple;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ServerPositionUpdateMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ServerPositionUpdateMessage.class);
    }
}