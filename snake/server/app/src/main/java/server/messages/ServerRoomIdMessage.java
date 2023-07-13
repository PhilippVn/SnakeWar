package server.messages;

import com.google.gson.Gson;

public class ServerRoomIdMessage {
    private String messageCode;
    private int roomId;
    private String timeStamp;

    public ServerRoomIdMessage() {
        this.messageCode = "room-id";
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public static ServerRoomIdMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ServerRoomIdMessage.class);
    }
}

