package server;

import javax.websocket.EndpointConfig;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/snake/{room-id}")
public class Room {
    @OnOpen
   public void open(Session session,
                    EndpointConfig c,
                    @PathParam("room-id") String roomName) {
      // Add the client to the chat room of their choice ...
   }
}
