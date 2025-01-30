package request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;

/**
 * The IncomingRequestListener class facilitates server-side listening for incoming
 * client connections over a network. It allows setting a port, establishing a connection
 * with a client, and optionally closing the server socket when communication is complete.
 */
public class IncomingRequestListener {

    private Optional<ServerSocket> serverSocket = Optional.empty();
    private static int portNumber;

    /**
     * Default constructor for the IncomingRequestListener class.
     * Initializes a new instance without any pre-configured server socket or port number.
     * This constructor does not start the listening process or configure any network properties.
     */
    public IncomingRequestListener() {

    }

    /**
     * Listens for incoming client connections on a specified port. If a connection is established,
     * it returns an {@link Optional} containing the communication {@link Socket} for the client.
     * If the connection fails or if the socket is reset, an empty {@link Optional} is returned.
     *
     * @return An {@link Optional} containing the connected {@link Socket}, or an empty {@link Optional}
     *         if the connection is not successful or the socket is reset.
     */
    public Optional<Socket> listen() {

        try {
            serverSocket = Optional.of(new ServerSocket(portNumber));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            return Optional.of(serverSocket.get().accept());    // if the client connection is successful create communication socket on server for client
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Terminates the server's connection by closing the ServerSocket instance if it is present and open.
     * This method ensures that the server stops listening for incoming connections by shutting down
     * the active ServerSocket. If the socket is already closed or does not exist, no action is taken.
     * Handles any {@link IOException} that may be thrown during socket closure and logs the error message.
     */
    public void terminateConnection() {
        if (serverSocket.isPresent() && !serverSocket.get().isClosed()) {
            try {
                serverSocket.get().close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Sets the port number to be used by the server socket.
     *
     * @param port the port number to be assigned for server communication
     */
    public void setPortNumber(int port)
    {
        portNumber = port;
    }
}
