package server.messages;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class ServerGameOverMessage implements Message {

    private String messageCode;
    private boolean winnerExists;
    private String winnerName;
    private LocalDateTime timeStamp;

    @Override
    public String toJson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd.MM.yyyy-HH.mm.ss")
                .create();
        return gson.toJson(this);
    }

    @Override
    public ServerGameOverMessage fromJson(String json) throws JsonSyntaxException {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd.MM.yyyy-HH.mm.ss")
                .create();
        return gson.fromJson(json, ServerGameOverMessage.class);
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public boolean isWinnerExists() {
        return winnerExists;
    }

    public void setWinnerExists(boolean winnerExists) {
        this.winnerExists = winnerExists;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

}
