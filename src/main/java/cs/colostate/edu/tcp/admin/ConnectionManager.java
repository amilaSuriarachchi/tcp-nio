package cs.colostate.edu.tcp.admin;

import cs.colostate.edu.tcp.admin.message.Message;
import cs.colostate.edu.tcp.admin.message.MessageFactory;
import cs.colostate.edu.tcp.admin.message.MessageProcessingException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ConnectionManager implements ConnectionCallback {

    // port of this node.
    private int port;
    // call back class to pass the messages to upper layer
    private MessageCallback messageCallback;

    public ConnectionManager() {
    }

    public ConnectionManager(int port, MessageCallback messageCallback) {
        this.port = port;
        this.messageCallback = messageCallback;
    }

    /**
     * this method open a connection if there is no connection to that ip address from and to that port. Otherwise use
     * existing one.
     *
     * @param message
     * @param ipAddress
     * @param port
     * @throws MessageProcessingException
     * @throws ConnectionException
     */
    public Message sendMessage(Message message, String ipAddress, int port)
            throws MessageProcessingException, ConnectionException {
        try {

            Socket socket = new Socket(ipAddress, port);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            message.serialize(dataOutputStream);
            socket.getOutputStream().flush();
            Message response = MessageFactory.getMessage(socket.getInputStream());
            if (!socket.isClosed()){
               socket.close();
            }
            return response;
        } catch (IOException e) {
            throw new ConnectionException("Can not connect to server ", e);
        }
    }

    /**
     * starts the connection listener and wait until it sets the correct port.
     */
    public void start() {
        ConnectionListener connectionListener = new ConnectionListener(this.port, this);
        Thread thread = new Thread(connectionListener);
        thread.start();

    }
    /**
     * When the connection listener accept a connection for a client it passes the socket to connection call back.
     * Once we get a socket connection we start a thread to receive packets countinuouly from that.
     *
     * @param socket
     * @throws ConnectionException
     */
    public void connectionAccepted(Socket socket) throws ConnectionException {
        MessageListener messageListener =
                new MessageListener(socket, this.messageCallback);
        Thread thread = new Thread(messageListener);
        thread.start();

    }



}
