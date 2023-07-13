package server.messages;

import com.google.gson.Gson;

public class ClientGameStopMessage {
    private String messageCode;
    private String clientName;
    private String timeStamp;

    public ClientGameStopMessage() {
        this.messageCode = "game-stop";
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public static ClientGameStopMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ClientGameStopMessage.class);
    }

}

