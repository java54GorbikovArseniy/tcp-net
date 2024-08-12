package telran.net;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class TcpServer {
    Protocol protocol;
    int port;
    public TcpServer(Protocol protocol, int port){
        this.port = port;
        this.protocol = protocol;
    }

    public void run(){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server is listening on port " + port);
            while (true){
                Socket socket = serverSocket.accept();
                TcpClientServerSession session = new TcpClientServerSession(socket, protocol);
                session.run();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
