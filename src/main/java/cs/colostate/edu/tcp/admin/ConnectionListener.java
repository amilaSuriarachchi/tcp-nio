package cs.colostate.edu.tcp.admin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectionListener implements Runnable {

    Logger logger = Logger.getLogger(ConnectionListener.class.getName());

    private int port;
    private ConnectionCallback connectionCallback;

    public ConnectionListener(int port, ConnectionCallback connectionCallback) {
        this.port = port;
        this.connectionCallback = connectionCallback;
    }

    public void run() {
        try {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(this.port);
                logger.log(Level.INFO, "Sucessfully connected to port " + this.port);

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Can not connect to the port ");
            }

            while (true) {
                Socket socket = serverSocket.accept();
                this.connectionCallback.connectionAccepted(socket);
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can not accept connections for port " + this.port, e);
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Can not process the message ", e);
        }
    }
}
