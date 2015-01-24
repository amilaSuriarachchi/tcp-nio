package cs.colostate.edu.tcp.admin;

import cs.colostate.edu.tcp.Constants;
import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.admin.message.*;
import cs.colostate.edu.tcp.client.Client;
import cs.colostate.edu.tcp.client.ClientManager;
import cs.colostate.edu.tcp.client.Stream;
import cs.colostate.edu.tcp.server.ServerManager;

import java.util.ArrayList;
import java.util.List;

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
    private Stream stream;

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
                break;
            }
            case Constants.INITIALIZE_MESSAGE_TYPE: {
                response = initialize((InitializeMessage) message);
                break;
            }

            case Constants.CLEAR_STAT_MESSAGE_TYPE: {
                response = clearStats();
                break;
            }
        }
        return response;

    }

    private Message clearStats() {
        this.serverManager.clearStats();
        return new ACKMessage("ok");
    }

    private Message initialize(InitializeMessage message) {
        List<Node> nodes = new ArrayList<Node>();
        for (String host : message.getHosts()) {
            nodes.add(new Node(message.getPort(), host));
        }
        ClientManager clientManager = new ClientManager();
        clientManager.start();
        this.stream = new Stream(nodes, clientManager);
        this.client.initializeConnections(this.stream);
        return new ACKMessage("ok");

    }

    private Message getSummary() {

        return new SummaryMessage(this.serverManager.getReceivedTotal(),
                this.serverManager.getTotalLatency(),
                this.client.getTotoalSend(),
                this.client.getTotalTime());

    }

    private Message startClient(StartClientMessage message) {

        ClientStarter clientStarter =
                new ClientStarter(this.stream,
                        message.getNumOfMessages(),
                        message.getNumberOfWorkers(),
                        message.getClientBuffer(),
                        this.client);

        Thread thread = new Thread(clientStarter);
        thread.start();

        return new ACKMessage("Got the message");
    }
}
