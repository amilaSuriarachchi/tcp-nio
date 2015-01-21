package cs.colostate.edu.tcp.message;

import cs.colostate.edu.tcp.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/20/15
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMessage implements Message {

    private int sequenceNo;
    private double time;
    private double value;
    private String streamID;
    private String senderID;
    private String receiverID;

    public TestMessage() {
    }

    public TestMessage(int sequenceNo, double time, double value, String streamID, String senderID, String receiverID) {
        this.sequenceNo = sequenceNo;
        this.time = time;
        this.value = value;
        this.streamID = streamID;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(this.sequenceNo);
            dataOutput.writeDouble(this.time);
            dataOutput.writeDouble(this.value);
            dataOutput.writeUTF(this.streamID);
            dataOutput.writeUTF(this.senderID);
            dataOutput.writeUTF(this.receiverID);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to data output ", e);
        }
    }

    public void read(DataInput dataInput) throws MessageProcessingException {
        try {
            this.sequenceNo = dataInput.readInt();
            this.time = dataInput.readDouble();
            this.value = dataInput.readDouble();
            this.streamID = dataInput.readUTF();
            this.senderID = dataInput.readUTF();
            this.receiverID = dataInput.readUTF();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ", e);
        }
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "TestMessage{" +
                "sequenceNo=" + sequenceNo +
                ", time=" + time +
                ", value=" + value +
                ", streamID='" + streamID + '\'' +
                ", senderID='" + senderID + '\'' +
                ", receiverID='" + receiverID + '\'' +
                '}';
    }
}
