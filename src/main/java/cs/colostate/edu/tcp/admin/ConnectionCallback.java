package cs.colostate.edu.tcp.admin;

import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/24/14
 * Time: 10:15 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ConnectionCallback {

    public void connectionAccepted(Socket socket) throws ConnectionException;
}
