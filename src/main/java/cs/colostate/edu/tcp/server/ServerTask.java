package cs.colostate.edu.tcp.server;

import cs.colostate.edu.tcp.message.Message;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.Configurator;
import cs.colostate.edu.tcp.message.TestMessage;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/23/14
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerTask implements Runnable {

    private Logger logger = Logger.getLogger(ServerTask.class.getName());

    private ServerConnection serverConnection;

    private double totalLatency = 0;
    private int totalMessages = 0;

    private MessageReceiver messageReceiver;

    public ServerTask(ServerConnection serverConnection, MessageReceiver messageReceiver) {
        this.serverConnection = serverConnection;
        this.messageReceiver = messageReceiver;
    }

    public void run() {
        DataInput dataInput = null;

        while (true) {
            dataInput = this.serverConnection.getDataInput();
            try {
                int messageSize = dataInput.readInt();
                byte[] message = new byte[messageSize];
                dataInput.readFully(message);
                this.serverConnection.releaseDataInput(dataInput);
                // process the messages
                processMessage(message);

            } catch (MessageProcessingException e) {
                this.logger.log(Level.SEVERE, "Can not parse the message");
            } catch (IOException e) {
                this.logger.log(Level.SEVERE, "Can not read the message");
            } catch (RuntimeException e) {
                this.logger.log(Level.SEVERE, "Can not read data from connection " + e.getMessage());
            }
        }
    }

    private void processMessage(byte[] byteMessage) throws MessageProcessingException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteMessage);
        DataInput dataInput = new DataInputStream(byteArrayInputStream);
        try {
            int numberOfMessages = dataInput.readInt();
            for (int i = 0; i < numberOfMessages; i++) {
                TestMessage message = new TestMessage();
                message.read(dataInput);
                this.messageReceiver.onMessage(message);
                this.totalLatency = this.totalLatency + System.currentTimeMillis() - message.getTime();
                this.totalMessages++;
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Problem in parsing the message");
        }
    }

    public void clearStats() {
        this.totalMessages = 0;
        this.totalLatency = 0;
    }

    public double getTotalLatency() {
        return totalLatency;
    }

    public int getTotalMessages() {
        return totalMessages;
    }
}
