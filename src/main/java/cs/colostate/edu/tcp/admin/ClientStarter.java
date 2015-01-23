package cs.colostate.edu.tcp.admin;

import cs.colostate.edu.tcp.client.Client;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/15
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientStarter implements Runnable {

    private String host;
    private int port;
    private int numOfMessages;
    private int numberOfWorkers;
    private int clientBuffer;

    public ClientStarter(String host, int port, int numOfMessages, int numberOfWorkers, int clientBuffer) {
        this.host = host;
        this.port = port;
        this.numOfMessages = numOfMessages;
        this.numberOfWorkers = numberOfWorkers;
        this.clientBuffer = clientBuffer;
    }

    public void run() {
        Client client = new Client();
        client.startClient(this.host, this.port, this.numOfMessages, this.numberOfWorkers, this.clientBuffer);
    }
}
