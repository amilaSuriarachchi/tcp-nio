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
 * Date: 8/18/14
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartClientMessage implements Message {

    private int numOfMessages;
    private int numberOfWorkers;
    private int clientBuffer;

    public StartClientMessage() {
    }

    public StartClientMessage(int numOfMessages, int numberOfWorkers, int clientBuffer) {
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
            dataOutput.writeInt(this.numOfMessages);
            dataOutput.writeInt(this.numberOfWorkers);
            dataOutput.writeInt(this.clientBuffer);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to writer ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.numOfMessages = dataInput.readInt();
            this.numberOfWorkers = dataInput.readInt();
            this.clientBuffer = dataInput.readInt();

        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message");
        }
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
