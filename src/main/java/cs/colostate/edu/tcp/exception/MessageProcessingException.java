package cs.colostate.edu.tcp.exception;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/17/14
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageProcessingException extends Exception {

    public MessageProcessingException() {
    }

    public MessageProcessingException(String message) {
        super(message);
    }

    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageProcessingException(Throwable cause) {
        super(cause);
    }

    public MessageProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
