package server.messages;

import java.time.LocalDateTime;

import com.google.gson.JsonSyntaxException;

public interface Message {
    String toJson();

    Message fromJson(String json) throws JsonSyntaxException;

    String getMessageCode();

    void setMessageCode(String messageCode);

    LocalDateTime getTimeStamp();

    void setTimeStamp(LocalDateTime timeStamp);
}
