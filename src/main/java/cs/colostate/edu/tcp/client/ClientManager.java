package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.message.Message;


import java.io.DataOutput;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/21/14
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientManager implements  FailureCallback{

    private Map<Node, ClientConnection> nodeToConnectionMap;
    private ClientIOReactor clientIOReactor;
    private List<FailureCallback> failureCallbacks;

    public ClientManager() {
        this.nodeToConnectionMap = new ConcurrentHashMap<Node, ClientConnection>();
        this.failureCallbacks = new Vector<FailureCallback>();
    }

    public void registerFailureCallback(FailureCallback failureCallback){
        this.failureCallbacks.add(failureCallback);
    }

    public void start(){
        //start the client io reactor.
        this.clientIOReactor = new ClientIOReactor();
        Thread clientThread = new Thread(this.clientIOReactor);
        clientThread.start();
    }

    public void addClientConnections(List<Node> targetNodes) {
        for (Node node : targetNodes) {
            addNodeConnection(node);
        }
    }

    public void sendEvent(Message message, Node targetNode) throws MessageProcessingException {

        ClientConnection clientConnection = this.nodeToConnectionMap.get(targetNode);
        if (clientConnection != null){
            DataOutput dataOutput = clientConnection.getDataOutput();
            message.serialize(dataOutput);
            clientConnection.releaseDataOutput(dataOutput);
        }
    }

    public void sendEvents(List<Message> messages, Node targetNode) throws MessageProcessingException {

        ClientConnection clientConnection = this.nodeToConnectionMap.get(targetNode);
        if (clientConnection != null){
            DataOutput dataOutput = clientConnection.getDataOutput();
            for (Message message : messages){
                message.serialize(dataOutput);
            }
            clientConnection.releaseDataOutput(dataOutput);
        }

    }

    private void addNodeConnection(Node targetNode) {
        if (!this.nodeToConnectionMap.containsKey(targetNode)) {
            synchronized (this.nodeToConnectionMap) {
                if (!this.nodeToConnectionMap.containsKey(targetNode)) {
                    ClientConnection clientConnection = new ClientConnection(targetNode);
                    clientConnection.setFailureCallback(this);
                    this.clientIOReactor.add(clientConnection);
                    this.nodeToConnectionMap.put(targetNode, clientConnection);
                }
            }
        }
    }

    public void nodeFailed(Node node) {
        this.nodeToConnectionMap.remove(node);
        for (FailureCallback failureCallback : this.failureCallbacks){
            failureCallback.nodeFailed(node);
        }
    }
}
