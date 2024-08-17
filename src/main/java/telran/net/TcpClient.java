package telran.net;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.*;

import static telran.net.TcpConfigurationProperties.*;

public class TcpClient implements Closeable{
    private static final long DEFAULT_INTERVAL = 3000;
    private static final int DEFAULT_NUMBER_ATTEMPTS = 10;
    String hostname;
    int port;
    Socket socket;
    BufferedReader receiver;
    PrintStream sender;
    long interval;
    int nAttempts;

    public TcpClient(String hostname, int port, long interval, int nAttempts) {
        this.hostname = hostname;
        this.port = port;
        this.interval = interval;
        this.nAttempts = nAttempts;
        if (this.interval < 0){
            this.interval = 0;
        }
        connect();
    }

    public TcpClient(String hostname, int port) {
        this(hostname, port, DEFAULT_INTERVAL, DEFAULT_NUMBER_ATTEMPTS);
    }

    private void connect() {
        int counter = nAttempts;
        do {
            try {
                socket = new Socket(hostname, port);
                sender = new PrintStream(socket.getOutputStream());
                receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                counter = 0;
            }catch (IOException e){
                waitForInterval();
                counter--;
            }
        }while (counter != 0);
    }

    private void waitForInterval() {
        Instant finished = Instant.now().plusMillis(interval);
        while (Instant.now().isBefore(finished));
    }



    @Override
    public void close() throws IOException {
        socket.close();
    }

    public String sendAndReceive(Request request){
        try {
            sender.println(request);
            String responseJSON = receiver.readLine();
            if (responseJSON == null){
                throw new RuntimeException("Server closed connection");
            }
            JSONObject jsonObject = new JSONObject(responseJSON);
            ResponseCode responseCode = jsonObject.getEnum(ResponseCode.class, RESPONSE_CODE_FIELD);
            String responseData = jsonObject.getString(RESPONSE_DATA_FIELD);
            if(responseCode != ResponseCode.OK){
                throw new RuntimeException(responseData);
            }
            return responseData;
        }catch (IOException e){
            connect();
            throw new RuntimeException("Server is unavailable, repeat later on");
        }
    }
}
