package cs.colostate.edu.tcp.server;

import cs.colostate.edu.tcp.Configurator;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/20/15
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    public void startServer(int port){
        ServerManager serverManager = new ServerManager(port);
        serverManager.start();
    }

    public static void main(String[] args) {
        Configurator configurator = Configurator.getInstance();

        int ioThreads = Integer.parseInt(args[1]);
        int workerThreads = Integer.parseInt(args[2]);
        int bufferSize = Integer.parseInt(args[3]);
        int taskBuffer = Integer.parseInt(args[4]);

        configurator.setValues(ioThreads, workerThreads, 20, bufferSize, taskBuffer);
        Server server = new Server();
        server.startServer(Integer.parseInt(args[0]));
    }
}
