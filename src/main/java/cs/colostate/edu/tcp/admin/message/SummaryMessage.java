package cs.colostate.edu.tcp.admin.message;

import cs.colostate.edu.tcp.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/23/15
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class SummaryMessage implements Message {

    private int totalReceived;
    private double totalLatency;
    private long totalSend;
    private long totalTime;

    public SummaryMessage() {
    }

    public SummaryMessage(int totalReceived, double totalLatency, long totalSend, long totalTime) {
        this.totalReceived = totalReceived;
        this.totalLatency = totalLatency;
        this.totalSend = totalSend;
        this.totalTime = totalTime;
    }

    public int getMessageType() {
        return Constants.SUMMARY_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
            dataOutput.writeInt(this.totalReceived);
            dataOutput.writeDouble(this.totalLatency);
            dataOutput.writeLong(this.totalSend);
            dataOutput.writeLong(this.totalTime);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write data ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.totalReceived = dataInput.readInt();
            this.totalLatency = dataInput.readDouble();
            this.totalSend = dataInput.readLong();
            this.totalTime = dataInput.readLong();
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read from stream ", e);
        }
    }

    public int getTotalReceived() {
        return totalReceived;
    }

    public double getTotalLatency() {
        return totalLatency;
    }

    public long getTotalSend() {
        return totalSend;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public double getLatency(){
        return this.totalLatency / this.totalReceived;
    }

    public double getThroughput(){
        return this.totalSend * 1000.0 / this.totalTime;
    }
}
