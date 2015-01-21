package cs.colostate.edu.tcp;

import cs.colostate.edu.tcp.message.Message;
import cs.colostate.edu.tcp.client.ClientManager;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.server.ServerManager;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/16/14
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommManager {

    private ClientManager clientManager;
    private ServerManager serverManager;

    public CommManager(int port) {
        this.clientManager = new ClientManager();
        this.serverManager = new ServerManager(port);
    }

    public void start() {
        this.serverManager.start();
        this.clientManager.start();

    }

    public void sendEvent(Message message, Node targetNode) throws MessageProcessingException {

        this.clientManager.sendEvent(message, targetNode);

    }

    public void sendEvents(List<Message> messages, Node targetNode) throws MessageProcessingException {

        this.clientManager.sendEvents(messages, targetNode);

    }
}
