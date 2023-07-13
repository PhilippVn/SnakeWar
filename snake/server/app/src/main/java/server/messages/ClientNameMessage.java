package server.messages;

import com.google.gson.Gson;

public class ClientNameMessage {
    private String messageCode;
    private String clientName;
    private String timeStamp;

    public ClientNameMessage() {
        this.messageCode = "client-name";
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

    public static ClientNameMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ClientNameMessage.class);
    }
}

