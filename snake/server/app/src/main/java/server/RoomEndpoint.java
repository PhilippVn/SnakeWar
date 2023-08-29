package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.NoSuchElementException;

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
public class RoomEndpoint {
   private String clientmsg = null;
   private boolean isConnected;
   private Session session;

   @OnOpen
   public void open(Session session,
         EndpointConfig c,
         @PathParam("room-id") String roomId) { 

      InetSocketAddress sip = (InetSocketAddress) session.getUserProperties()
            .get("javax.websocket.endpoint.remoteAddress");
      InetAddress ip = sip.getAddress();

      // check if its a valid room id
      try {
         RoomHandler room = SnakeServer.rooms.stream().filter(r -> r.getRoomId().equals(roomId)).findFirst().get();
         // valid room id
         // add room to room handler
         if (room.isFull()) {
            // room is full
            System.out.println(ip + " tried to join full room: " + roomId);
            try {
               session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Room is full"));
            } catch (IOException ifnored) {
            }
            return;
         }
         this.isConnected = true;
         this.session = session;
         // add roomendpoint to roomhandler
         if (!room.hasFirstRoomEndpoint()) {
            room.setRoomEndpoint1(this);
            room.setPlayer1(new Player(session, ip));
            System.out.println("Added first player to room");
         } else {
            // check if its the same player trying to connect two times
            /* 
            if (room.getPlayer1().getIp().equals(ip)) {
               System.out.println(ip + " tried to connect two times to the room: " + roomId);
               try {
                  session.close(
                        new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Only one Room connection allowed"));
               } catch (IOException ifnored) {
               }
               return;
            } */
            
            room.setRoomEndpoint2(this);
            room.setPlayer2(new Player(session, ip));
            System.out.println("Added second player to room");
         }

      } catch (NoSuchElementException e) {
         // invalid roomId
         System.out.println(ip + " tried to access invalid room id: " + roomId);
         try {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Invalid Room id"));
         } catch (IOException ifnored) {
         }
      }

   }

   @OnMessage
   public void onMessage(Session session, String msg) {
      this.clientmsg = msg;
   }

   

   public Session getSession() {
      return session;
   }

   @OnClose
   public void onClose(Session session, CloseReason reason) {
      // if one player closes the session the game ends
      isConnected = false;
      System.out.println("Player disconnected from room");
   }

   @OnError
   public void onError(Session session, Throwable error) {
      System.err.println("There was an Error:" + error.getMessage());
      error.printStackTrace();
      try {
          session.close();
      } catch (IOException ignored) {
      }
   }

   public boolean hasClientMessage(){
      return this.clientmsg != null;
   }

   public String readClientmsg() {
      String msg = this.clientmsg;
      this.clientmsg = null;
      return msg;
   }

   public void sendClientMsg(String msg){
      try {
         this.session.getBasicRemote().sendText(msg);
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public boolean isConnected() {
      return isConnected;
   }

   public static String generateRoomId() {
      return RoomNumberGenerator.generateRoomNumber();
   }

   private class RoomNumberGenerator {
      private static final String ROOM_NUMBER_CHARACTERS = "123456789abcdefghijklmnopqrstuvwxyz";
      private static final int ROOM_NUMBER_LENGTH = 4;

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

   @Override
   public String toString() {
      return "RoomEndpoint [clientmsg=" + clientmsg + ", isConnected=" + isConnected + "]";
   }

}
