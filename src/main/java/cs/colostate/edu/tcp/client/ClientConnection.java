package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.exception.MessageProcessingException;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
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

    private DataOutput dataOutput;

    private Lock lock;
    private Condition condition;

    private FailureCallback failureCallback;

    private boolean isClosed;

    public ClientConnection(Node targetNode) {
        this.targetNode = targetNode;
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.isClosed = false;
    }

    public void setFailureCallback(FailureCallback failureCallback) {
        this.failureCallback = failureCallback;
    }

    public void sendMessage(byte[] message) throws MessageProcessingException {

        this.lock.lock();
        try {

            if (this.isClosed) {
                throw new MessageProcessingException("Connection is closed ");
            }

            if (this.dataOutput == null) {
                try {
                    this.condition.await();
                } catch (InterruptedException e) {
                }
            }
            this.dataOutput.writeInt(message.length);
            this.dataOutput.write(message);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not send message ");
        } finally {
            this.lock.unlock();
        }

    }

    public void registerSelectionKey(SelectionKey selectionKey) {
        OutputStream outputStream = new DataWritter(this);
        //remove the current attachment
        selectionKey.attach(outputStream);
        this.lock.lock();
        this.dataOutput = new DataOutputStream(outputStream);
        this.condition.signalAll();
        this.lock.unlock();

    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
    }

    public void close() {
        this.isClosed = true;
        this.failureCallback.nodeFailed(this.targetNode);
        this.lock.lock();
        this.condition.signalAll();
        this.lock.unlock();
    }
}