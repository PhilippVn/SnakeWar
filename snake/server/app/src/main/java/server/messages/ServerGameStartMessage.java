package server.messages;

import com.google.gson.Gson;

public class ServerGameStartMessage {
    private String messageCode;
    private String timeStamp;

    public ServerGameStartMessage() {
        this.messageCode = "game-start";
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
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

    public static ServerGameStartMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ServerGameStartMessage.class);
    }
}

