package cs.colostate.edu.tcp.admin;


import cs.colostate.edu.tcp.admin.message.Message;


public interface MessageCallback {

    public Message messageReceived(Message message);
}
