package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;


// Each instance of an endpoint class is associated with one and only one connection and peer
@ServerEndpoint("/snake")
public class SnakeServer{

    private Session s; // session used for responding without a request
    private static final java.util.concurrent.CopyOnWriteArrayList<String> users = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<String,Pair<String,Integer>> players = new ConcurrentHashMap<>(); // map of all playing users
    public static final int PORT = 51036;

    @OnOpen
    public void onOpen(Session session,
                 EndpointConfig conf) throws IOException {
        // Get session and WebSocket connection
        this.s = session;
        var ip = session.getUserProperties().get("javax.websocket.endpoint.remoteAddress");
        System.out.println("New Connection:" + ip);
        System.out.println(session.getUserProperties().toString());
    }

    @OnMessage
    public void onMessage(Session session,  String msg) throws IOException {
        // Handle new messages
        System.out.println("Recieved Message: " + msg);
        try {
            session.getBasicRemote().sendText("Server response to:" + msg +  "\n>>> Hello from Server!");
         } catch (IOException e) { 
            throw new RuntimeException(e);
         }
    }

	
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        if(!reason.getReasonPhrase().isEmpty())
            System.out.println("Connection closed: " + reason.getReasonPhrase());
        else
            System.out.println("Connection closed.");
    }

	
    @OnError
    public void onError(Session session, Throwable error) {

    }

    public static void main(String[] args) throws Exception{
        Server server = new Server(PORT);

        // Create a ServletContextHandler with the given context path.
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        server.setHandler(handler);

        // Add Entpoints
        WebSocketServerContainerInitializer.configure(handler, (servletContext, wsContainer) -> {
            wsContainer.addEndpoint(SnakeServer.class);
        });

        // start server
        server.start();

        // print websocket adress
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        String address = "ws://" + ipAddress + ":" + PORT + "/snake";
        System.out.println("WebSocket address:" + address);
    }
}