package cs.colostate.edu.tcp.admin;

import cs.colostate.edu.tcp.Constants;
import cs.colostate.edu.tcp.admin.message.ACKMessage;
import cs.colostate.edu.tcp.admin.message.Message;
import cs.colostate.edu.tcp.admin.message.StartClientMessage;
import cs.colostate.edu.tcp.admin.message.SummaryMessage;
import cs.colostate.edu.tcp.client.Client;
import cs.colostate.edu.tcp.server.ServerManager;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/18/14
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminService implements MessageCallback {

    private ServerManager serverManager;
    private Client client;

    public AdminService(ServerManager serverManager) {
        this.serverManager = serverManager;
        this.client = new Client();
    }

    public Message messageReceived(Message message) {
        Message response = null;
        switch (message.getMessageType()) {

            case Constants.START_CLIENT_MESSAGE_TYPE: {
                response = startClient((StartClientMessage) message);
                break;
            }
            case Constants.SUMMARY_REQUEST_MESSAGE_TYPE: {
                response = getSummary();
            }
        }
        return response;

    }

    private Message getSummary() {

        return new SummaryMessage(this.serverManager.getReceivedTotal(),
                this.serverManager.getTotalLatency(),
                this.client.getTotoalSend(),
                this.client.getTotalTime());

    }

    private Message startClient(StartClientMessage message) {

        ClientStarter clientStarter =
                new ClientStarter(message.getHosts(),
                        message.getPort(),
                        message.getNumOfMessages(),
                        message.getNumberOfWorkers(),
                        message.getClientBuffer(),
                        this.client);

        Thread thread = new Thread(clientStarter);
        thread.start();

        return new ACKMessage("Got the message");
    }
}
