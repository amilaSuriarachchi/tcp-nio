package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/15
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Stream {

    private int nextNodeToAssign;

    // this is the physical worker this stream has to send the message.
    protected List<Node> nodes;

    // communication manager for this worker. All underline communications should happen through this.
    protected ClientManager clientManager;

    public Stream(List<Node> nodes,
                             ClientManager clientManager) {
        this.nodes = nodes;
        this.clientManager = clientManager;
        this.nextNodeToAssign = 0;
    }

    public void emit(List<Message> messages) throws MessageProcessingException {

        Map<Node, List<Message>> nodeMessageMap = new HashMap<Node, List<Message>>();
        // populate this for all nodes
        for (int i = 0; i < this.nodes.size(); i++) {
            nodeMessageMap.put(this.nodes.get(i), new ArrayList<Message>());
        }

        for (Message message : messages){
            nodeMessageMap.get(getNode()).add(message);
        }

        for (Node node : this.nodes){
            this.clientManager.sendEvents(nodeMessageMap.get(node), node);
        }
    }

    private synchronized Node getNode() {
        Node nextNode = this.nodes.get(this.nextNodeToAssign);
        this.nextNodeToAssign = (this.nextNodeToAssign + 1) % this.nodes.size();
        return nextNode;
    }
}
