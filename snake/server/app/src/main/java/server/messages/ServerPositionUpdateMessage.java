package server.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import server.Apple;
import server.LocalDateTimeTypeAdapter;
import server.Snake;

import java.time.LocalDateTime;

public class ServerPositionUpdateMessage implements Message {
    private String messageCode;
    private int player1Length;
    private int player2Length;
    private Snake player1Snake;
    private Snake player2Snake;
    private Apple apple;
    private LocalDateTime timeStamp;

    // Getters and Setters

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        return gson.toJson(this);
    }

    public ServerPositionUpdateMessage fromJson(String json) throws JsonSyntaxException {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        return gson.fromJson(json, ServerPositionUpdateMessage.class);
    }

    @Override
    public String getMessageCode() {
        return this.messageCode;
    }

    @Override
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    @Override
    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }


    public int getPlayer1Length() {
        return player1Length;
    }

    public void setPlayer1Length(int player1Length) {
        this.player1Length = player1Length;
    }

    public int getPlayer2Length() {
        return player2Length;
    }

    public void setPlayer2Length(int player2Length) {
        this.player2Length = player2Length;
    }

    public Snake getPlayer1Snake() {
        return player1Snake;
    }

    public void setPlayer1Snake(Snake player1Snake) {
        this.player1Snake = player1Snake;
    }

    public Snake getPlayer2Snake() {
        return player2Snake;
    }

    public void setPlayer2Snake(Snake player2Snake) {
        this.player2Snake = player2Snake;
    }

    public Apple getApple() {
        return apple;
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

}