package server.messages;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class ClientInputMessage implements Message {
    private String messageCode;
    private String clientName;
    private int input;
    private LocalDateTime timeStamp;

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

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
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

    public ClientInputMessage fromJson(String json) throws JsonSyntaxException {

        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy-HH.mm.ss").create();
        return gson.fromJson(json, ClientInputMessage.class);
    }
}
