package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;


// Each instance of an endpoint class is associated with one and only one connection and peer
@ServerEndpoint("/snake")
public class SnakeServer{

    private Session s; // session used for responding without a request
    private static final java.util.concurrent.CopyOnWriteArrayList<String> users = new CopyOnWriteArrayList<>(); // list of ip adresses
    private static final ConcurrentHashMap<String,Pair<String,Integer>> players = new ConcurrentHashMap<>(); // map of all playing users
    public static final int PORT = 51036;

    @OnOpen
    public void onOpen(Session session,
                 EndpointConfig conf) {
        // Get session and WebSocket connection
        this.s = session;
        InetSocketAddress ip = (InetSocketAddress) session.getUserProperties().get("javax.websocket.endpoint.remoteAddress");
        if(users.contains(ip.getAddress().getHostAddress())){
            try {
                session.getBasicRemote().sendText("U may only connect once!");
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Double Connection"));
            } catch (IOException ignored) {
            }
            
            return;
        }
        users.add(ip.getAddress().getHostAddress());
        System.out.println("New Connection:" + ip);
        System.out.println(session.getUserProperties().toString());
    }

    @OnMessage
    public void onMessage(Session session,  String msg){
        // Handle new messages
        System.out.println("Recieved Message: " + msg);
        try {
            String roomcode = Room.generateRoomId();
            session.getBasicRemote().sendText("Here is your room code:" + roomcode);

           Room.rooms.putIfAbsent(roomcode, new Pair());
         } catch (IOException e) { 
            throw new RuntimeException(e);
         }
    }

	
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        if(!reason.getReasonPhrase().isEmpty())
            System.out.println("Connection closed. Reason:" + reason.getReasonPhrase());
        else
            System.out.println("Connection closed.");
        
        users.remove(((InetSocketAddress) session.getUserProperties().get("javax.websocket.endpoint.remoteAddress")).getAddress().getHostAddress());
    }

	
    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("There was an Error:" + error.getMessage());
        try {
            session.close();
        } catch (IOException ignored) {}
    }

    public static void main(String[] args) throws Exception{
        Server server = new Server(PORT);

        // Create a ServletContextHandler with the given context path.
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        server.setHandler(handler);

        // Add Entpoints
        WebSocketServerContainerInitializer.configure(handler, (servletContext, wsContainer) -> {
            wsContainer.addEndpoint(SnakeServer.class);
            wsContainer.addEndpoint(Room.class);
        });

        // start server
        server.start();

        // print websocket adress
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        String address = "ws://" + ipAddress + ":" + PORT + "/snake";
        System.out.println("WebSocket address:" + address);
    }
}