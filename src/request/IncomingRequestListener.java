package request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

        while (true) {

            try {

                if (serverSocket.isPresent()) {
                    return Optional.of(serverSocket.get().accept());    // if the client connection is successful create communication socket on server for client
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return Optional.empty();
        }
    }

    public void setPortNumber(int port)
    {
        portNumber = port;
    }
}
