package cs.colostate.edu.tcp.server;

import cs.colostate.edu.tcp.Configurator;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/21/14
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerManager {

    private ServerIOReactor serverIOReactor;
    private int port;

    public ServerManager(int port) {
        this.port = port;
    }

    public void start() {

        ServerConnection serverConnection = new ServerConnection();
        // start the Server Task pool
        for (int i = 0; i < Configurator.getInstance().getWorkerPoolSize(); i++) {
            ServerTask serverTask = new ServerTask(serverConnection);
            Thread thread = new Thread(serverTask);
            thread.start();
        }

        this.serverIOReactor = new ServerIOReactor(this.port, serverConnection);
        Thread serverThread = new Thread(this.serverIOReactor);
        serverThread.start();

    }

}
