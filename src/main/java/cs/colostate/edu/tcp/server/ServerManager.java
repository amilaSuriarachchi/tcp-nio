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
    private ServerTask[] tasks;

    public ServerManager(int port) {
        this.port = port;
    }

    public void start() {

        ServerConnection serverConnection = new ServerConnection();
        this.tasks = new ServerTask[Configurator.getInstance().getWorkerPoolSize()];

        // start the Server Task pool
        for (int i = 0; i < Configurator.getInstance().getWorkerPoolSize(); i++) {
            this.tasks[i] = new ServerTask(serverConnection);
            Thread thread = new Thread(this.tasks[i]);
            thread.start();
        }

        this.serverIOReactor = new ServerIOReactor(this.port, serverConnection);
        Thread serverThread = new Thread(this.serverIOReactor);
        serverThread.start();

    }

    public int getReceivedTotal() {
        int total = 0;
        for (ServerTask task : this.tasks) {
            total += task.getTotalMessages();
        }
        return total;
    }

    public double getTotalLatency() {
        double total = 0;
        for (ServerTask task : this.tasks) {
            total += task.getTotalLatency();
        }
        return total;
    }

    public void clearStats() {
        for (ServerTask task : this.tasks) {
            task.clearStats();
        }
    }


}
