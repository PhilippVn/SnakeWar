package server.messages;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import server.LocalDateTimeTypeAdapter;

public class ServerGameStartMessage implements Message {
    private String messageCode;
    private int playerNumber;
    private LocalDateTime timeStamp;

    public String getMessageCode() {
        return messageCode;
    }

    

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        return gson.toJson(this);
    }

    public ServerGameStartMessage fromJson(String json) throws JsonSyntaxException {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        return gson.fromJson(json, ServerGameStartMessage.class);
    }



    public int getPlayerNumber() {
        return playerNumber;
    }



    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
