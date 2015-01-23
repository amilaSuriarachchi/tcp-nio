package cs.colostate.edu.tcp.admin;

import cs.colostate.edu.tcp.admin.message.Message;
import cs.colostate.edu.tcp.admin.message.MessageFactory;
import cs.colostate.edu.tcp.admin.message.MessageProcessingException;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class continuously listens to the incoming messages and pass them back to higher layers.
 * first it create the message using message factory and pass that back to call back.
 */
public class MessageListener implements Runnable {

    Logger logger = Logger.getLogger(MessageListener.class.getName());

    private Socket socket;
    private MessageCallback messageCallback;

    public MessageListener(Socket socket, MessageCallback messageCallback) {
        this.socket = socket;
        this.messageCallback = messageCallback;
    }

    public void run() {
            try {

                Message request = MessageFactory.getMessage(socket.getInputStream());
                Message response = this.messageCallback.messageReceived(request);
                DataOutput dataOutput = new DataOutputStream(socket.getOutputStream());
                response.serialize(dataOutput);
                socket.getOutputStream().flush();

            } catch (MessageProcessingException e) {
                //TODO: think what to do ? do we need to close the connection or clean it up.
                this.logger.log(Level.SEVERE, "Can not process the message");
            } catch (IOException e) {
                //TODO: think what to do ? do we need to close the connection or clean it up.
                this.logger.log(Level.SEVERE, "Can not process the message");
            }
    }
}
