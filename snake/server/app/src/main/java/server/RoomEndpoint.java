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
         isConnected = true;
         // add roomendpoint to roomhandler
         if (!room.hasFirstRoomEndpoint()) {
            room.setRoomEndpoint1(this);
            room.setPlayer1(new Player(session, ip));
         } else {
            // check if its the same player trying to connect two times
            if (room.getPlayer1().getIp().equals(ip)) {
               System.out.println(ip + " tried to connect two times to the room: " + roomId);
               try {
                  session.close(
                        new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Only one Room connection allowed"));
               } catch (IOException ifnored) {
               }
               return;
            }

            room.setRoomEndpoint2(this);
            room.setPlayer2(new Player(session, ip)); // starts the room thread
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

   @OnClose
   public void onClose(Session session, CloseReason reason) {
      // if one player closes the session the game ends
      isConnected = false;
   }

   @OnError
   public void onError(Session session, Throwable error) {

   }

   public String readClientmsg() {
      String msg = this.clientmsg;
      this.clientmsg = null;
      return msg;
   }

   public boolean isConnected() {
      return isConnected;
   }

   public static String generateRoomId() {
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

   @Override
   public String toString() {
      return "RoomEndpoint [clientmsg=" + clientmsg + ", isConnected=" + isConnected + "]";
   }

}
