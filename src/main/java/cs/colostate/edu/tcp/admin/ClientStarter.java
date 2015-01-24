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

    private Stream stream;
    private int numOfMessages;
    private int numberOfWorkers;
    private int clientBuffer;
    private Client client;

    public ClientStarter(Stream stream,
                         int numOfMessages,
                         int numberOfWorkers,
                         int clientBuffer,
                         Client client) {
        this.stream = stream;
        this.numOfMessages = numOfMessages;
        this.numberOfWorkers = numberOfWorkers;
        this.clientBuffer = clientBuffer;
        this.client = client;
    }

    public void run() {
        this.client.startClient(this.stream, this.numOfMessages, this.numberOfWorkers, this.clientBuffer);
    }
}
