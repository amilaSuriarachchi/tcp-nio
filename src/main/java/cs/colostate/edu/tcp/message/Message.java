package cs.colostate.edu.tcp.message;

import cs.colostate.edu.tcp.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/20/15
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Message {

    public void serialize(DataOutput dataOutput) throws MessageProcessingException;

    public void read(DataInput dataInput) throws MessageProcessingException;
}
