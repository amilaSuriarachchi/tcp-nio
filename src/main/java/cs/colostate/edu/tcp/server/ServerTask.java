package cs.colostate.edu.tcp.server;

import cs.colostate.edu.tcp.message.Message;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.Configurator;
import cs.colostate.edu.tcp.message.TestMessage;

import java.io.DataInput;
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
        List<TestMessage> messages = new ArrayList<TestMessage>();
        for (int i = 0; i < Configurator.getInstance().getTaskBufferMessages(); i++) {
            messages.add(new TestMessage());
        }

        while (true) {
            dataInput = this.serverConnection.getDataInput();
            try {
                try {
                    for (TestMessage message : messages){
                        message.read(dataInput);
                        this.messageReceiver.onMessage(message);
                        this.totalLatency = this.totalLatency + System.currentTimeMillis() - message.getTime();
                        this.totalMessages++;
                    }
                } catch (MessageProcessingException e) {
                    this.logger.log(Level.SEVERE, "Can not parse the message");
                }
                this.serverConnection.releaseDataInput(dataInput);

            } catch (RuntimeException e) {
                this.logger.log(Level.SEVERE, "Can not read data from connection " + e.getMessage());

            }
        }
    }

    public void clearStats(){
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
