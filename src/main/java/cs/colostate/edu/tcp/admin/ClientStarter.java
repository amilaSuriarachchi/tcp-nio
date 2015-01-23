package cs.colostate.edu.tcp.admin;

import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.client.Client;
import cs.colostate.edu.tcp.client.ClientManager;
import cs.colostate.edu.tcp.client.Stream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/15
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientStarter implements Runnable {

    private List<String> hosts;
    private int port;
    private int numOfMessages;
    private int numberOfWorkers;
    private int clientBuffer;
    private Client client;

    public ClientStarter(List<String> hosts,
                         int port,
                         int numOfMessages,
                         int numberOfWorkers,
                         int clientBuffer,
                         Client client) {
        this.hosts = hosts;
        this.port = port;
        this.numOfMessages = numOfMessages;
        this.numberOfWorkers = numberOfWorkers;
        this.clientBuffer = clientBuffer;
        this.client = client;
    }

    public void run() {

        List<Node> nodes = new ArrayList<Node>();
        for (String host : this.hosts) {
            nodes.add(new Node(this.port, host));
        }

        ClientManager clientManager = new ClientManager();
        clientManager.start();
        Stream stream = new Stream(nodes, clientManager);
        this.client.startClient(stream, this.numOfMessages, this.numberOfWorkers, this.clientBuffer);
    }
}
