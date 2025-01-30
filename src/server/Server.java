package server;

import request.IncomingRequestListener;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;

public class Server {

    static final int PORT = 8000;
    static Optional<Socket> socket = Optional.empty();

    public Server() {

    }

    private void receiveMessage() {
        Optional<BufferedReader> reader = Optional.empty();

        try {
            if (socket.isPresent()) {
                reader = Optional.of(new BufferedReader(new InputStreamReader(socket.get().getInputStream())));

                String message = reader.get().readLine();
                System.out.println(message);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (reader.isPresent()) {
                reader = Optional.empty();
            }
        }
    }

    public void run() {
        IncomingRequestListener requestListener = new IncomingRequestListener();
        requestListener.setPortNumber(PORT);

        socket = requestListener.listen();
        while (true) {
            receiveMessage();
            requestListener.terminateConnection();
        }

    }
}
