package cs.colostate.edu.tcp.admin.message;

import cs.colostate.edu.tcp.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/18/14
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartClientMessage implements Message {

    private String host;
    private int port;
    private int numOfMessages;
    private int numberOfWorkers;
    private int clientBuffer;

    public StartClientMessage() {
    }

    public StartClientMessage(String host, int port, int numOfMessages, int numberOfWorkers, int clientBuffer) {
        this.host = host;
        this.port = port;
        this.numOfMessages = numOfMessages;
        this.numberOfWorkers = numberOfWorkers;
        this.clientBuffer = clientBuffer;
    }

    public int getMessageType() {
        return Constants.START_CLIENT_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(Constants.START_CLIENT_MESSAGE_TYPE);
            dataOutput.writeUTF(this.host);
            dataOutput.writeInt(this.port);
            dataOutput.writeInt(this.numOfMessages);
            dataOutput.writeInt(this.numberOfWorkers);
            dataOutput.writeInt(this.clientBuffer);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to writer ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.host = dataInput.readUTF();
            this.port = dataInput.readInt();
            this.numOfMessages = dataInput.readInt();
            this.numberOfWorkers = dataInput.readInt();
            this.clientBuffer = dataInput.readInt();

        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message");
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getNumOfMessages() {
        return numOfMessages;
    }

    public void setNumOfMessages(int numOfMessages) {
        this.numOfMessages = numOfMessages;
    }

    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public void setNumberOfWorkers(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public int getClientBuffer() {
        return clientBuffer;
    }

    public void setClientBuffer(int clientBuffer) {
        this.clientBuffer = clientBuffer;
    }
}
