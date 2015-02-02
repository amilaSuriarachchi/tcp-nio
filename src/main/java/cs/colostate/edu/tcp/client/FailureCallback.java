package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Node;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/29/15
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FailureCallback {

    public void nodeFailed(Node node);
}
