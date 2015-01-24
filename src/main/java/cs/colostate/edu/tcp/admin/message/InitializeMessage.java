package cs.colostate.edu.tcp.admin.message;

import cs.colostate.edu.tcp.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/23/15
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class InitializeMessage implements Message {

    private List<String> hosts;
    private int port;

    public InitializeMessage() {
    }

    public InitializeMessage(List<String> hosts, int port) {
        this.hosts = hosts;
        this.port = port;
    }

    public int getMessageType() {
        return Constants.INITIALIZE_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeInt(this.hosts.size());
            for (String host : this.hosts) {
                dataOutput.writeUTF(host);
            }
            dataOutput.writeInt(this.port);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write data ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {

        try {
            int size = dataInput.readInt();
            this.hosts = new ArrayList<String>(size);
            for (int i = 0; i < size; i++) {
                this.hosts.add(dataInput.readUTF());
            }
            this.port = dataInput.readInt();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read data ", e);
        }

    }

    public List<String> getHosts() {
        return hosts;
    }

    public int getPort() {
        return port;
    }
}
