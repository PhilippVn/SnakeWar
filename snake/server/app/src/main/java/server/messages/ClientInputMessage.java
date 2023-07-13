package server.messages;

import com.google.gson.Gson;

public class ClientInputMessage {
    private String messageCode;
    private String clientName;
    private int input;
    private String timeStamp;

    public ClientInputMessage() {
        this.messageCode = "client-input";
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

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
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

    public static ClientInputMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ClientInputMessage.class);
    }
}

