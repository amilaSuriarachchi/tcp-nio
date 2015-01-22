package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Node;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.Queue;

/**
 * this class holds the
 */
public class ClientConnection {

    private Node targetNode;

    private Queue<DataOutput> freeDataOutputs;

    public ClientConnection(Node targetNode) {
        this.targetNode = targetNode;
        this.freeDataOutputs = new LinkedList<DataOutput>();
    }

    public synchronized DataOutput getDataOutput(){
        DataOutput returnDataOutput = null;
        while ((returnDataOutput = this.freeDataOutputs.poll()) == null){
            try {
                this.wait();
            } catch (InterruptedException e) {
                //TODO: handle this properly
            }
        }
        return returnDataOutput;
    }

    public synchronized void releaseDataOutput(DataOutput dataOutput){
         this.freeDataOutputs.add(dataOutput);
         this.notifyAll();
    }

    public synchronized void registerSelectionKey(SelectionKey selectionKey){
        OutputStream outputStream = new DataWritter();
        DataOutput dataOutput = new DataOutputStream(outputStream);
        //remove the current attachment
        selectionKey.attach(outputStream);
        this.freeDataOutputs.add(dataOutput);
        this.notifyAll();

    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
    }
}
