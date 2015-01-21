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

    public ServerTask(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void run() {
        DataInput dataInput = null;
        List<Message> messages = new ArrayList<Message>();
        for (int i = 0; i < Configurator.getInstance().getTaskBufferMessages(); i++) {
            messages.add(new TestMessage());
        }

        while (true) {
            dataInput = this.serverConnection.getDataInput();
            try {
                for (Message message : messages){
                    message.read(dataInput);
                }
                this.serverConnection.releaseDataInput(dataInput);
            } catch (MessageProcessingException e) {
                this.logger.log(Level.SEVERE, "Can not parse the message");
            }
        }
    }
}
