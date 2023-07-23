package server;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;

import server.messages.ClientInputMessage;
import server.messages.ClientNameMessage;
import server.messages.ServerGameOverMessage;
import server.messages.ServerGameStartMessage;
import server.messages.ServerPositionUpdateMessage;

/** This class represents a game room and handles two rooms for two players */
public class RoomHandler implements Runnable {
    private RoomProtocolStage protocolStage = RoomProtocolStage.CLIENT_NAME_MESSAGE;
    private final String roomId;
    private RoomEndpoint roomEndpoint1 = null;
    private RoomEndpoint roomEndpoint2 = null;
    private Player player1 = null;
    private Player player2 = null;
    private Apple apple;
    private Player winner;
    private boolean gameOver = false;
    private boolean shouldLogMessages = false;

    public RoomHandler(String roomId) {
        this.roomId = roomId;
    }

    public Apple getApple() {
        return apple;
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    public void setRoomEndpoint1(RoomEndpoint roomEndpoint1) {
        this.roomEndpoint1 = roomEndpoint1;
    }

    public void setRoomEndpoint2(RoomEndpoint roomEndpoint2) {
        this.roomEndpoint2 = roomEndpoint2;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
        // start this room
        // sanity check
        if (!isFull()) {
            System.err.println(toString());
            throw new IllegalStateException("Player two was set but somehow the room wasnt full. Should not happen");
        }
        SnakeServer.startRoom(this);
    }

    public String getRoomId() {
        return roomId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean hasFirstRoomEndpoint() {
        return this.roomEndpoint1 != null;
    }

    public boolean hasSecondRoomEndpoint() {
        return this.roomEndpoint2 != null;
    }

    public boolean hasFirstPlayer() {
        return this.player1 != null;
    }

    public boolean hasSecondPlayer() {
        return this.player2 != null;
    }

    public boolean isFull() {
        return hasFirstPlayer() && hasSecondPlayer() && hasFirstRoomEndpoint() && hasSecondRoomEndpoint();
    }

    public void closeRoom(CloseReason.CloseCode code, String msg) {
        try {
            roomEndpoint1.getSession().close(new CloseReason(code, msg));
            roomEndpoint2.getSession().close(new CloseReason(code, msg));
        } catch (IOException ignored) {
        }
    }

    @Override
    public void run() {
        System.out.print("ROOM STARTED");
        LocalDateTime last = LocalDateTime.now();
        boolean shouldRun = true;
        // TODO implement Game Loop and Message Protocol
        while (roomEndpoint1.isConnected() && roomEndpoint2.isConnected() && shouldRun) {
            switch (protocolStage) {
                case CLIENT_GAME_STOP_REQUEST:
                    break;
                case CLIENT_INPUT_MESSAGE:
                    while (!roomEndpoint1.hasClientMessage() || !roomEndpoint2.hasClientMessage()) {
                        try {
                            Thread.sleep(100);
                            System.out.println("Thread sleeps for user input...");
                        } catch (InterruptedException ignored) {
                        }
                    }

                    LocalDateTime now = LocalDateTime.now();
                    long timeDelta = Duration.between(last, now).toMillis();

                    while(timeDelta < GameBoard.GAME_DEFAULT_GAME_SPEED/GameBoard.GAME_SPEED_MODIFIER){
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ignored) {
                        }
                        now = LocalDateTime.now();
                        timeDelta = Duration.between(last, now).toMillis();
                    }
                    last = now;

                    String cimsg1 = roomEndpoint1.readClientmsg();
                    String cimsg2 = roomEndpoint2.readClientmsg();
                    logClientMsg(cimsg1);
                    logClientMsg(cimsg2);

                    ClientInputMessage ci1 = new ClientInputMessage().fromJson(cimsg1);
                    ClientInputMessage ci2 = new ClientInputMessage().fromJson(cimsg2);

                    int inputPlayer1 = ci1.getInput();
                    int inputPlayer2 = ci2.getInput();

                    // moving

                    Snake.Segment snake1Head = player1.getSnake().getSegments().get(player1.getSnake().getSegments().size()-1);
                    Snake.Segment snake2Head = player2.getSnake().getSegments().get(player2.getSnake().getSegments().size()-1);

                    int[] newPos1 = handleUserinput(snake1Head.getX(),snake1Head.getY(),inputPlayer1);
                    int[] newPos2 = handleUserinput(snake2Head.getX(),snake2Head.getY(),inputPlayer2);

                    player1.getSnake().move(newPos1[0],newPos1[1]);
                    player2.getSnake().move(newPos2[0],newPos2[1]);

                    // collision detection
                    boolean snake1AteApple = this.apple.detectCollision(player1.getSnake());
                    boolean snake2AteApple = this.apple.detectCollision(player2.getSnake());

                    if(snake1AteApple){
                        player1.getSnake().addRemoved();
                    }

                    if(snake2AteApple){
                        player2.getSnake().addRemoved();
                    }

                    boolean snake1HitSnake2 = player1.getSnake().detectCollision(player2.getSnake());
                    boolean snake2HitSnake1 = player2.getSnake().detectCollision(player1.getSnake());

                    if(snake1HitSnake2 && snake2HitSnake1){
                        // no winner
                        gameOver = true;
                        winner = null;
                        
                    }

                    else if(snake1HitSnake2){
                        // TODO GAME OVER -> SNAKE 2 WON
                        gameOver = true;
                        winner = player2;
                        System.out.println("Snake 1 is controlled by:" + player1.getName() + " and collided.\n>>> The winner is: " + winner.getName());
                        
                    }

                    else if(snake2HitSnake1){
                        // TODO GAME OVER -> SNAKE 1 WON
                        gameOver = true;
                        winner = player1;
                        System.out.println("Snake 2 is controlled by:" + player2.getName() + " and collided.\\n" + 
                                ">>> The winner is: " + winner.getName());
                        
                    }


                    

                    // new apple?

                    if(snake1AteApple || snake2AteApple){
                        int[] newAppleSpawnLocation = Apple.spawnApple(player1.getSnake(), player2.getSnake());
                        apple = new Apple(newAppleSpawnLocation[0], newAppleSpawnLocation[1]);
                    }

                    // update scores
                    player1.setScore(player1.getSnake().getSegments().size());
                    player2.setScore(player2.getSnake().getSegments().size());

                    // sending updates
                    protocolStage = RoomProtocolStage.SERVER_POSITION_UPDATE_MESSAGE;

                    break;
                case CLIENT_NAME_MESSAGE:
                    while (!roomEndpoint1.hasClientMessage() || !roomEndpoint2.hasClientMessage()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    String cnmsg1 = roomEndpoint1.readClientmsg();
                    String cnmsg2 = roomEndpoint2.readClientmsg();
                    logClientMsg(cnmsg1);
                    logClientMsg(cnmsg2);

                    ClientNameMessage cnm1 = new ClientNameMessage().fromJson(cnmsg1);
                    ClientNameMessage cnm2 = new ClientNameMessage().fromJson(cnmsg2);

                    if (!cnm1.getMessageCode().equals("client-name") || !cnm2.getMessageCode().equals("client-name")) {
                        closeRoom(CloseReason.CloseCodes.PROTOCOL_ERROR, "Expected client name message.");
                    }

                    player1.setName(cnm1.getClientName());
                    player2.setName(cnm2.getClientName());
                    protocolStage = RoomProtocolStage.SERVER_GAME_START_MESSAGE;
                    break;
                case SERVER_GAME_START_MESSAGE:
                    ServerGameStartMessage sgs1 = new ServerGameStartMessage(); //TODO check if player numbers are correct
                    sgs1.setMessageCode("game-start");
                    sgs1.setPlayerNumber(1);
                    sgs1.setTimeStamp(LocalDateTime.now());

                    ServerGameStartMessage sgs2 = new ServerGameStartMessage();
                    sgs2.setMessageCode("game-start");
                    sgs2.setPlayerNumber(2);
                    sgs2.setTimeStamp(LocalDateTime.now());

                    logServerMsg(sgs1.toJson());
                    logServerMsg(sgs2.toJson());
                    roomEndpoint1.sendClientMsg(sgs1.toJson());
                    roomEndpoint2.sendClientMsg(sgs2.toJson());

                    // send initial positions of snakes and apple

                    ServerPositionUpdateMessage spu = new ServerPositionUpdateMessage();
                    int[] spawnLocation1 = Snake.spawnSnake(null);
                    player1.setSnake(new Snake(spawnLocation1[0], spawnLocation1[1]));
                    int[] spawnLocation2 = Snake.spawnSnake(player1.getSnake());
                    player2.setSnake(new Snake(spawnLocation2[0], spawnLocation2[1]));

                    player1.setScore(player1.getSnake().getLength());
                    player2.setScore(player2.getSnake().getLength());

                    int[] appleSpawnLocation = Apple.spawnApple(player1.getSnake(), player2.getSnake());
                    apple = new Apple(appleSpawnLocation[0], appleSpawnLocation[1]);

                    spu.setMessageCode("position-update");
                    spu.setApple(apple);
                    spu.setPlayer1Length(player1.getScore());
                    spu.setPlayer2Length(player2.getScore());
                    spu.setPlayer1Snake(player1.getSnake());
                    spu.setPlayer2Snake(player2.getSnake());
                    spu.setTimeStamp(LocalDateTime.now());

                    logServerMsg(spu.toJson());

                    roomEndpoint1.sendClientMsg(spu.toJson());
                    roomEndpoint2.sendClientMsg(spu.toJson());

                    protocolStage = RoomProtocolStage.CLIENT_INPUT_MESSAGE;
                    break;
                case SERVER_POSITION_UPDATE_MESSAGE:
                    ServerPositionUpdateMessage msgspu = new ServerPositionUpdateMessage();
                    msgspu.setMessageCode("position-update");
                    msgspu.setApple(apple);
                    msgspu.setPlayer1Length(player1.getScore());
                    msgspu.setPlayer2Length(player2.getScore());
                    msgspu.setPlayer1Snake(player1.getSnake());
                    msgspu.setPlayer2Snake(player2.getSnake());
                    msgspu.setTimeStamp(LocalDateTime.now());
                    logServerMsg(msgspu.toJson());

                    roomEndpoint1.sendClientMsg(msgspu.toJson());
                    roomEndpoint2.sendClientMsg(msgspu.toJson());
                    if(gameOver){
                        protocolStage = RoomProtocolStage.Server_Game_OVER_MESSAGE;
                    }else{
                        protocolStage = RoomProtocolStage.CLIENT_INPUT_MESSAGE;
                    }
                    
                    break;
                case Server_Game_OVER_MESSAGE:
                    ServerGameOverMessage sgo = new ServerGameOverMessage();
                    sgo.setMessageCode("game-over");
                    if(winner != null){
                        sgo.setWinnerExists(true);
                        sgo.setWinnerName(winner.getName());
                    }else{
                        sgo.setWinnerExists(false);
                        sgo.setWinnerName("");
                    }
                    sgo.setTimeStamp(LocalDateTime.now());

                    logServerMsg(sgo.toJson());

                    roomEndpoint1.sendClientMsg(sgo.toJson());
                    roomEndpoint2.sendClientMsg(sgo.toJson());
                    protocolStage = RoomProtocolStage.UNKNOWN;
                    System.out.println("Sent game over message!");
                    break;
                default:
                    System.out.println("unknown protocol stage");
                    shouldRun = false;
                    break;
            }
        }

        if(roomEndpoint1.isConnected() && roomEndpoint2.isConnected()){
            try {
                roomEndpoint1.getSession().close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Room closed"));
                roomEndpoint2.getSession().close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Room closed"));
            } catch (IOException ignored) {
            }
        }
        else if(roomEndpoint1.isConnected()){
            try {
                roomEndpoint1.getSession().close(new CloseReason(CloseCodes.CLOSED_ABNORMALLY, "One of the players disconnected from room"));
            } catch (IOException ignored) {
            }
        }else if(roomEndpoint2.isConnected()){
             try {
                roomEndpoint2.getSession().close(new CloseReason(CloseCodes.CLOSED_ABNORMALLY, "One of the players disconnected from room"));
            } catch (IOException ignored) {
            }
        }

        System.out.println("Closing room...");
        SnakeServer.rooms.remove(this);
        System.out.println("Number of active rooms: " + SnakeServer.rooms.size());
    }

    private int[] handleUserinput(int x, int y, int input) {
        int[] ret;
        switch (input) {
            case 87: // W
            case 119 : ret = new int[]{x,y-GameBoard.GAME_STEPSIZE};break;// w
            case 65:
            case 97: ret = new int[]{x-GameBoard.GAME_STEPSIZE,y};break;// a
            case 83:
            case 115: ret = new int[]{x,y+GameBoard.GAME_STEPSIZE};break;// s
            case 68:
            case 100: ret = new int[]{x+GameBoard.GAME_STEPSIZE,y};break;// d
            default: System.err.println("Invalid Client Input:" + (char)input); ret = new int[]{x,y};break; // dont move
        }

        // implement wall teleportation

        if (ret[1] < 0) {
            ret[1] = GameBoard.GAME_HEIGHT - GameBoard.GAME_STEPSIZE;
        } else if (ret[1] >= GameBoard.GAME_HEIGHT) {
            ret[1] = 0;
        }
    
        if (ret[0] < 0) {
            ret[0] = GameBoard.GAME_WIDTH - GameBoard.GAME_STEPSIZE;
        } else if (ret[0] >= GameBoard.GAME_WIDTH) {
            ret[0] = 0;
        }

        return ret;
    }

    @Override
    public String toString() {
        return "RoomHandler [protocolStage=" + protocolStage + ", roomId=" + roomId + ", roomEndpoint1=" + roomEndpoint1
                + ", roomEndpoint2=" + roomEndpoint2 + ", player1=" + player1 + ", player2=" + player2 + "]";
    }

    public void logClientMsg(String msg) {
        if(shouldLogMessages)
            System.out.println("Message from Client:" + msg);
    }

    public void logServerMsg(String msg) {
        if(shouldLogMessages)
        System.out.println("Message from Server:" + msg);
    }

}
