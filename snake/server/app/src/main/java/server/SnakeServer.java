package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.google.gson.JsonSyntaxException;

import server.messages.ClientRoomRequestMessage;
import server.messages.ServerRoomIdMessage;

// Each instance of an endpoint class is associated with one and only one connection and peer
@ServerEndpoint("/snake")
public class SnakeServer {

    public static final int PORT = 51036;

    public static final java.util.concurrent.CopyOnWriteArrayList<RoomHandler> rooms = new CopyOnWriteArrayList<>(); // list
                                                                                                                     // of
                                                                                                                     // active
                                                                                                                     // rooms
    public static final java.util.concurrent.CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>(); // list
                                                                                                                  // active
                                                                                                                  // players

    @OnOpen
    public void onOpen(Session session,
            EndpointConfig conf) {
        // Get session and WebSocket connection
        InetSocketAddress sip = (InetSocketAddress) session.getUserProperties()
                .get("javax.websocket.endpoint.remoteAddress");
        InetAddress ip = sip.getAddress();

        for (Player player : players) {
            if (player.getIp().equals(ip)) {
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY,
                            "Multiply Connections are not allowed."));
                } catch (IOException ignored) {
                }
            }
        }
        Player newPlayer = new Player(session, ip);
        players.add(newPlayer);
        System.out.println("New Player connected: " + newPlayer);
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        // TODO create a Room and a Room Handler

        // the first message should be a room request
        try {
            ClientRoomRequestMessage m = new ClientRoomRequestMessage().fromJson(msg);
            // check message code
            if (!m.getMessageCode().equals("room-request")) {
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR,
                            "Expected Room Request, but got:" + msg));
                } catch (IOException ignored) {
                }

                return;
            }

            // send room number
            String roomId = RoomEndpoint.generateRoomId();
            ServerRoomIdMessage srim = new ServerRoomIdMessage();
            srim.setMessageCode("room-id");
            srim.setRoomId(roomId);
            srim.setTimeStamp(LocalDateTime.now());
            try {
                session.getBasicRemote().sendText(srim.toJson());
                rooms.add(new RoomHandler(roomId));
            } catch (IOException e) {
                try {
                    session.close(
                            new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, "Error while sending Room id:"));
                } catch (IOException ignored) {
                }
            }

        } catch (JsonSyntaxException e) {
            // invalid protocol
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "Malformed JSON:" + msg));
            } catch (IOException ignored) {
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {

        Player player = players.stream().filter(p -> p.getSession().equals(session)).findFirst().get();

        if (!reason.getReasonPhrase().isEmpty())
            System.out.println(player + "disconnected. Reason: " + reason.getReasonPhrase());
        else
            System.out.println(player + "disconnected.");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("There was an Error:" + error.getMessage());
        try {
            session.close();
        } catch (IOException ignored) {
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);

        // Create a ServletContextHandler with the given context path.
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        server.setHandler(handler);

        // Add Entpoints
        WebSocketServerContainerInitializer.configure(handler, (servletContext, wsContainer) -> {
            wsContainer.addEndpoint(SnakeServer.class);
            wsContainer.addEndpoint(RoomEndpoint.class);
        });

        // start server
        server.start();

        // print websocket adress
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        String address = "ws://" + ipAddress + ":" + PORT + "/snake";
        System.out.println("WebSocket address:" + address);
    }

    public static void startRoom(RoomHandler roomHandler) {
        new Thread(roomHandler).start();
    }
}