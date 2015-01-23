package cs.colostate.edu.tcp;

import cs.colostate.edu.tcp.admin.AdminService;
import cs.colostate.edu.tcp.admin.ConnectionManager;
import cs.colostate.edu.tcp.server.Server;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/15
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Worker {

     private void startWorker(int workerPort, int adminPort){
         Server server = new Server();
         server.startServer(workerPort);

         ConnectionManager connectionManager = new ConnectionManager(adminPort, new AdminService());
         connectionManager.start();
     }



    public static void main(String[] args) {

        int workerPort = Integer.parseInt(args[0]);
        int adminPort = Integer.parseInt(args[1]);

        int ioThreads = Integer.parseInt(args[2]);
        int workerThreads = Integer.parseInt(args[3]);
        int connections = Integer.parseInt(args[4]);
        int bufferSize = Integer.parseInt(args[5]);
        int taskBuffer = Integer.parseInt(args[6]);
        Configurator configurator = Configurator.getInstance();
        configurator.setValues(ioThreads, workerThreads, connections, bufferSize, taskBuffer);

        Worker worker = new Worker();
        worker.startWorker(workerPort, adminPort);

    }
}
