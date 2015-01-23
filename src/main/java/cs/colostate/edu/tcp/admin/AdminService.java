package cs.colostate.edu.tcp.admin;

import cs.colostate.edu.tcp.Constants;
import cs.colostate.edu.tcp.admin.message.ACKMessage;
import cs.colostate.edu.tcp.admin.message.Message;
import cs.colostate.edu.tcp.admin.message.StartClientMessage;
import cs.colostate.edu.tcp.client.Client;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/18/14
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminService implements MessageCallback {

    public Message messageReceived(Message message) {
        Message response = null;
        switch (message.getMessageType()) {

            case Constants.START_CLIENT_MESSAGE_TYPE: {
                response = startClient((StartClientMessage) message);
                break;
            }
        }
        return response;

    }

    private Message startClient(StartClientMessage message) {

        ClientStarter clientStarter =
                new ClientStarter(message.getHost(),
                        message.getPort(),
                        message.getNumOfMessages(),
                        message.getNumberOfWorkers(),
                        message.getClientBuffer());

        Thread thread = new Thread(clientStarter);
        thread.start();

        return new ACKMessage("Got the message");
    }
}
