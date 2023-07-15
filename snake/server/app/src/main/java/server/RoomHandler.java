package server;

/** This class represents a game room and handles two rooms for two players */
public class RoomHandler implements Runnable {
    private RoomProtocolStage protocolStage;
    private final String roomId;
    private RoomEndpoint roomEndpoint1 = null;
    private RoomEndpoint roomEndpoint2 = null;
    private Player player1 = null;
    private Player player2 = null;

    public RoomHandler(String roomId) {
        this.roomId = roomId;
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
        SnakeServer.startRoom(this); // starts the Thread
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

    @Override
    public void run() {
        // TODO implement Game Loop and Message Protocol
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    @Override
    public String toString() {
        return "RoomHandler [protocolStage=" + protocolStage + ", roomId=" + roomId + ", roomEndpoint1=" + roomEndpoint1
                + ", roomEndpoint2=" + roomEndpoint2 + ", player1=" + player1 + ", player2=" + player2 + "]";
    }

}
