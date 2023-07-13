package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/snake/room/{room-id}")
public class Room {
   public static final ConcurrentHashMap<String,Pair<Player,Player>> rooms = new ConcurrentHashMap<>(); // Mapping of room codes to Players

   @OnOpen
   public void open(Session session,
                    EndpointConfig c,
                    @PathParam("room-id") String roomId) {
      
      // check if its a valid room id

      Pair<Player,Player> players = rooms.get(roomId);
      if(players == null){
         // invalid room id
         System.out.println("Client tried to connect with invalid room code:" + roomId);
         try {
            session.close(); // close session
         } catch (IOException ignored) {
         }
         return;
      }

      // add the player to the room
      if(players.isFull()){
         System.out.println("Room is already full");
         try {
            session.close(); // close session
         } catch (IOException ignored) {
         }
         return;
      }

      InetSocketAddress sip = (InetSocketAddress) session.getUserProperties().get("javax.websocket.endpoint.remoteAddress");
      InetAddress ip = sip.getAddress();

      if(!players.hasFirst()){
         players.setFirst(new Player(session,ip));
         System.out.println("First player " + ip + " connected to room:" + roomId);
      }else{
         players.setSecond(new Player(session,ip));
         System.out.println("Second player " + ip + " connected to room:" + roomId);
      }
   }

   @OnMessage
   public void onMessage(Session session,  String msg){
         // TODO implement game protocol
   }

   @OnClose
    public void onClose(Session session, CloseReason reason) {
        
    }

	
    @OnError
    public void onError(Session session, Throwable error) {

    }


   public static String generateRoomId(){
      return RoomNumberGenerator.generateRoomNumber();
   }

   private class RoomNumberGenerator {
    private static final String ROOM_NUMBER_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ROOM_NUMBER_LENGTH = 16;

    public static String generateRoomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder roomNumberBuilder = new StringBuilder();

        for (int i = 0; i < ROOM_NUMBER_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(ROOM_NUMBER_CHARACTERS.length());
            char randomChar = ROOM_NUMBER_CHARACTERS.charAt(randomIndex);
            roomNumberBuilder.append(randomChar);
        }

        return roomNumberBuilder.toString();
      }
   }
}
