package cs.colostate.edu.tcp.server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/23/14
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerConnection {

    private Queue<DataInput> freeDataInputs;

    public ServerConnection() {
        this.freeDataInputs = new LinkedList<DataInput>();
    }

    public synchronized DataInput getDataInput(){
        DataInput returnDataInput = null;
        while ((returnDataInput = this.freeDataInputs.poll()) == null){
            try {
                this.wait();
            } catch (InterruptedException e) {
                //TODO: handle this properly
            }
        }
        return returnDataInput;
    }

    public synchronized void releaseDataInput(DataInput dataInput){
        this.freeDataInputs.add(dataInput);
        this.notifyAll();
    }

    public synchronized void registerSelectionKey(SelectionKey selectionKey){
        DataReader dataReader = new DataReader();
        DataInput dataInput = new DataInputStream(dataReader);
        //remove the current attachment
        selectionKey.attach(dataReader);
        this.freeDataInputs.add(dataInput);
        this.notifyAll();
    }
}
