package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/15
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Stream implements FailureCallback {

    private int nextNodeToAssign;

    // this is the physical worker this stream has to send the message.
    protected List<Node> nodes;

    // communication manager for this worker. All underline communications should happen through this.
    protected ClientManager clientManager;

    private Logger logger = Logger.getLogger(Stream.class.getName());

    public Stream(List<Node> nodes, ClientManager clientManager) {
        this.nodes = nodes;
        this.clientManager = clientManager;
        this.nextNodeToAssign = 0;
        this.clientManager.registerFailureCallback(this);
        this.clientManager.addClientConnections(nodes);
    }

    public void emit(List<Message> messages) throws MessageProcessingException {

        Map<Node, List<Message>> nodeMessageMap = new HashMap<Node, List<Message>>();
        synchronized (this) {
            // populate this for all nodes
            for (int i = 0; i < this.nodes.size(); i++) {
                nodeMessageMap.put(this.nodes.get(i), new ArrayList<Message>());
            }
        }

        Node node = null;
        for (Message message : messages) {
            node = getNode();
            if (node != null) {
                nodeMessageMap.get(node).add(message);
            }
        }

        for (Map.Entry<Node, List<Message>> entry : nodeMessageMap.entrySet()) {
            try {
                this.clientManager.sendEvents(entry.getValue(), entry.getKey());
            } catch (MessageProcessingException e) {
                this.logger.log(Level.SEVERE, "Can not send the message to " + entry.getKey().getIpAddress() + " " + e.getMessage());
            }
        }
    }

    private synchronized Node getNode() {
        if (this.nodes.size() > 0) {
            Node nextNode = this.nodes.get(this.nextNodeToAssign);
            this.nextNodeToAssign = (this.nextNodeToAssign + 1) % this.nodes.size();
            return nextNode;
        } else {
            return null;
        }

    }

    public synchronized void nodeFailed(Node node) {
        this.nodes.remove(node);
        if (this.nodes.size() > 0) {
            this.nextNodeToAssign = this.nextNodeToAssign % this.nodes.size();
        }
    }
}
