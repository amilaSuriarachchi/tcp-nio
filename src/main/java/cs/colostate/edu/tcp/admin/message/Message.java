package cs.colostate.edu.tcp.admin.message;

import java.io.DataInput;
import java.io.DataOutput;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/18/14
 * Time: 8:59 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Message {

    /**
     *
     * @return  - type of the message.
     */
    public int getMessageType();

    /**
     * serialise the message data to a data Output.
     * @param dataOutput
     * @throws MessageProcessingException
     */
    public void serialize(DataOutput dataOutput) throws MessageProcessingException;

    /**
     * read the message from the data Input
     * @param dataInput
     * @throws MessageProcessingException
     */
    public void parse(DataInput dataInput) throws MessageProcessingException;

}
