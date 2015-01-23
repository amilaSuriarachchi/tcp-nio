package cs.colostate.edu.tcp.admin.message;



import cs.colostate.edu.tcp.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/18/14
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class ACKMessage implements Message {

    private String message;

    public ACKMessage() {
    }

    public ACKMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return Constants.ACK_MESSAGE_TYPE;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeInt(Constants.ACK_MESSAGE_TYPE);
            dataOutput.writeUTF(this.message);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ", e);
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.message = dataInput.readUTF();
        } catch (IOException e) {
            throw new MessageProcessingException("can not read the message ", e);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
