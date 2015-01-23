package cs.colostate.edu.tcp.admin.message;

import cs.colostate.edu.tcp.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/23/15
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SummaryRequestMessage implements Message {

    public int getMessageType() {
        return Constants.SUMMARY_REQUEST_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(getMessageType());
        } catch (IOException e) {
            throw new MessageProcessingException("can not write data ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {

    }
}
