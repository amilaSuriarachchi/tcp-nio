package cs.colostate.edu.tcp.admin;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/23/14
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionException extends Exception {

    public ConnectionException() {
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionException(Throwable cause) {
        super(cause);
    }

    public ConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
