package request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;

public class IncomingRequestListener {

    private Optional<ServerSocket> serverSocket = Optional.empty();
    private static int portNumber;

    public IncomingRequestListener() {

    }

    public Optional<Socket> listen() {

        try {
            serverSocket = Optional.of(new ServerSocket(portNumber));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            return Optional.of(serverSocket.get().accept());    // if the client connection is successful create communication socket on server for client
        } catch (SocketException e) {
            if (e.getMessage().equals("Connection reset")) {
                return Optional.empty();
            }
        } catch (IOException e) {

        }
        return Optional.empty();
    }

    public void terminateConnection() {
        if (serverSocket.isPresent() && !serverSocket.get().isClosed()) {
            try {
                serverSocket.get().close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void setPortNumber(int port)
    {
        portNumber = port;
    }
}
