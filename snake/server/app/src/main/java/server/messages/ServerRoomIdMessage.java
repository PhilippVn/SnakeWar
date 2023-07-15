package server.messages;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class ServerRoomIdMessage implements Message {
    private String messageCode;
    private String roomId;
    private LocalDateTime timeStamp;

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd.MM.yyyy-HH.mm.ss")
                .create();
        return gson.toJson(this);
    }

    public ServerRoomIdMessage fromJson(String json) throws JsonSyntaxException {

        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy-HH.mm.ss").create();
        return gson.fromJson(json, ServerRoomIdMessage.class);
    }
}
