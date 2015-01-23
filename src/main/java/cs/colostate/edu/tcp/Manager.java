package cs.colostate.edu.tcp;

import cs.colostate.edu.tcp.admin.ConnectionException;
import cs.colostate.edu.tcp.admin.ConnectionManager;
import cs.colostate.edu.tcp.admin.message.MessageProcessingException;
import cs.colostate.edu.tcp.admin.message.StartClientMessage;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/22/15
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Manager {

    private void startManager(String[] workers,
                              int workerPort,
                              int adminPort,
                              int numberOfMsgs,
                              int numberOfWorkers,
                              int clientBuffer) {

        // send messages to workers to start the workers                         ,
        ConnectionManager connectionManager = new ConnectionManager();

        //create the start client message

        try {
            StartClientMessage startClientMessage =
                    new StartClientMessage("lattice-50", workerPort, numberOfMsgs, numberOfWorkers, clientBuffer);
            connectionManager.sendMessage(startClientMessage, "lattice-51", adminPort);
            startClientMessage =
                    new StartClientMessage("lattice-51", workerPort, numberOfMsgs, numberOfWorkers, clientBuffer);
            connectionManager.sendMessage(startClientMessage, "lattice-50", adminPort);
        } catch (MessageProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ConnectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public static void main(String[] args) {
        String[] workers = args[0].split(",");
        int workerPort = Integer.parseInt(args[1]);
        int adminPort = Integer.parseInt(args[2]);

        int numberOfMsgs = Integer.parseInt(args[3]);
        int numberOfWorkers = Integer.parseInt(args[4]);
        int clientBuffer = Integer.parseInt(args[5]);

        Manager manager = new Manager();
        manager.startManager(workers, workerPort, adminPort, numberOfMsgs, numberOfWorkers, clientBuffer);


    }
}
