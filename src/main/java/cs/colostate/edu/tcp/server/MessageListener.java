package cs.colostate.edu.tcp.server;


import cs.colostate.edu.tcp.message.Message;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/19/14
 * Time: 9:50 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MessageListener {

     public void onMessage(Message message);
}
