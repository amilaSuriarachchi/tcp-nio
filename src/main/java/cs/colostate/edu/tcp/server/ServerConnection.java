package cs.colostate.edu.tcp.server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/23/14
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerConnection {

    private Queue<DataInput> freeDataInputs;
    private Lock lock;
    private Condition condition;

    public ServerConnection() {
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.freeDataInputs = new LinkedList<DataInput>();
    }

    public DataInput getDataInput() {
        DataInput returnDataInput = null;
        this.lock.lock();
        while ((returnDataInput = this.freeDataInputs.poll()) == null) {
            try {
                this.condition.await();
            } catch (InterruptedException e) {
                //TODO: handle this properly
            }
        }
        this.lock.unlock();
        return returnDataInput;
    }

    public void releaseDataInput(DataInput dataInput) {
        this.lock.lock();
        this.freeDataInputs.add(dataInput);
        this.condition.signalAll();
        this.lock.unlock();
    }

    public void registerSelectionKey(SelectionKey selectionKey) {

        DataReader dataReader = new DataReader((SocketChannel)selectionKey.channel());
        DataInput dataInput = new DataInputStream(dataReader);
        //remove the current attachment
        selectionKey.attach(dataReader);
        this.lock.lock();
        this.freeDataInputs.add(dataInput);
        this.condition.signalAll();
        this.lock.unlock();
    }
}
