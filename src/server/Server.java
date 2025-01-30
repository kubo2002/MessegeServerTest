package server;

import request.IncomingRequestListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

public class Server {

    static final int PORT = 8000;
    static Optional<Socket> socket = Optional.empty();

    public Server() {

    }

    private void receiveMessage() {

        try {
            if (socket.isPresent()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.get().getInputStream()));

                String message = reader.readLine();
                System.out.println(message);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run()
    {
        IncomingRequestListener requestListener = new IncomingRequestListener();
        requestListener.setPortNumber(PORT);
        socket = requestListener.listen();   // server waits for client connection

        while (true) {
            receiveMessage();
        }

    }
}
