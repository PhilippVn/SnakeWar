package server.messages;

import com.google.gson.Gson;

public class ClientRoomRequestMessage {
    private String messageCode;
    private String timeStamp;

    public ClientRoomRequestMessage() {
        this.messageCode = "room-request";
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

    public static ClientRoomRequestMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ClientRoomRequestMessage.class);
    }
}
