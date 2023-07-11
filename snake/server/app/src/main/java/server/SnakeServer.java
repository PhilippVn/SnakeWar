package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;


// Each instance of an endpoint class is associated with one and only one connection and peer
@ServerEndpoint("/snake")
public class SnakeServer{

    private Session s; // session used for responding without a request
    private static final ConcurrentHashMap<String,Pair<String,Integer>> players = new ConcurrentHashMap<>();

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
        System.out.println("Recieved Message: " + msg + " from");
        try {
            session.getBasicRemote().sendText("Server response:" + msg);
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
        Server server = new Server(0); // Set the desired port for Jetty

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);

        WebSocketServerContainerInitializer.configure(contextHandler, (servletContext, wsContainer) -> {
            wsContainer.addEndpoint(SnakeServer.class);
        });

        server.start();

        int port = server.getURI().getPort();
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        String address = "ws://" + ipAddress + ":" + port + "/snake";
        System.out.println("WebSocket address: " + address);

        server.join();
    }
}