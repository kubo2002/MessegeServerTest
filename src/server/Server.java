package server;

import request.IncomingRequestListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

/**
 * The Server class is responsible for handling incoming client communications on
 * a designated port. It continuously listens for connections, reads incoming
 * messages, and terminates the connection after processing. The class utilizes
 * an instance of the IncomingRequestListener to manage socket-level operations.
 */
public class Server {

    private static final int PORT = 8000;
    private static Optional<Socket> socket = Optional.empty();
    private static IncomingRequestListener requestListener = new IncomingRequestListener();
    private boolean paused = false;


    /**
     * Constructs a new instance of the Server class.
     *
     * This constructor initializes the server's configuration by setting the port number
     * for the IncomingRequestListener instance using the default port specified by the
     * constant PORT. The port number determines where the server will listen for incoming
     * client requests.
     *
     * This initialization prepares the server for operation but does not start the server
     * or begin listening for connections. To initiate the server's operation, the {@code run}
     * method must be explicitly invoked.
     */
    public Server() {
        requestListener.setPortNumber(PORT);
    }


    /**
     * Reads and processes a single message from the connected client through the socket input stream.
     * This method checks if a socket connection is present, reads data using a buffered reader,
     * and outputs the received message to the standard output.
     *
     * If an error occurs during the reading process, it sets the server to a "paused" state,
     * indicating an issue with the current connection. The method ensures that resources are
     * properly cleaned up by resetting the buffered reader reference, even in case of exceptions.
     *
     * The method does not handle thread-safety or concurrent access and assumes that it is called
     * in a controlled environment where the socket and `paused` state are managed externally.
     *
     * This method is typically invoked within the server's main processing loop and is an integral
     * part of message handling for the client-server communication.
     */
    private void receiveMessage() {
        Optional<BufferedReader> reader = Optional.empty();

        try {
            if (socket.isPresent()) {
                reader = Optional.of(new BufferedReader(new InputStreamReader(socket.get().getInputStream())));

                String message = reader.get().readLine();
                System.out.println("Client : " + message);
            }
        } catch (Exception e) {
            paused = true;

        } finally {
            if (reader.isPresent()) {
                reader = Optional.empty();
            }
        }
    }


    /**
     * Starts the server's main processing loop. This method listens for incoming client connections,
     * processes received messages, and terminates connections in a continuous cycle.
     *
     * The server operates as follows:
     * - Continuously listens for incoming connections using the {@link IncomingRequestListener#listen} method.
     * - Upon connection, the server resets the paused state and enters a secondary loop that:
     *      - Reads and processes client messages using the {@code receiveMessage} method.
     *      - Terminates the client connection with {@link IncomingRequestListener#terminateConnection}.
     * - If an error occurs during the message processing or connection, the loop is paused, and the server
     *   prepares to accept a new connection.
     *
     * This method represents the core operating procedure of the server, running indefinitely until
     * externally stopped. It ensures that only one client connection is processed at a time and relies
     * on proper handling of socket connections to maintain operational stability.
     *
     * Thread-safety is not handled within the method, so external mechanisms must manage concurrent
     * access if this method is used in a multi-threaded environment.
     */
    public void run() {

        while (true) {
            socket = requestListener.listen();
            paused = false;
            while (!paused) {
                receiveMessage();
                requestListener.terminateConnection();
            }
        }
    }
}
