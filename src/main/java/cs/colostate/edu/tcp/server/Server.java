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
        configurator.setValues(1, 20, 8, 8192, 20);

        Server server = new Server();
        server.startServer(Integer.parseInt(args[0]));
    }
}
