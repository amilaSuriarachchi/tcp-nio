package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Node;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * this class holds the
 */
public class ClientConnection {

    private Node targetNode;

    private Queue<DataOutput> freeDataOutputs;

    private Lock lock;
    private Condition condition;

    public ClientConnection(Node targetNode) {
        this.targetNode = targetNode;
        this.freeDataOutputs = new LinkedList<DataOutput>();
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    public DataOutput getDataOutput() {
        DataOutput returnDataOutput = null;
        this.lock.lock();
        while ((returnDataOutput = this.freeDataOutputs.poll()) == null) {
            try {
                this.condition.await();
            } catch (InterruptedException e) {
                //TODO: handle this properly
            }
        }
        this.lock.unlock();
        return returnDataOutput;
    }

    public void releaseDataOutput(DataOutput dataOutput) {
        this.lock.lock();
        this.freeDataOutputs.add(dataOutput);
        this.condition.signalAll();
        this.lock.unlock();
    }

    public void registerSelectionKey(SelectionKey selectionKey) {
        OutputStream outputStream = new DataWritter();
        DataOutput dataOutput = new DataOutputStream(outputStream);
        //remove the current attachment
        selectionKey.attach(outputStream);
        this.lock.lock();
        this.freeDataOutputs.add(dataOutput);
        this.condition.signalAll();
        this.lock.unlock();

    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
    }
}
