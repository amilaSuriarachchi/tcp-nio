package cs.colostate.edu.tcp;

import cs.colostate.edu.tcp.admin.ConnectionException;
import cs.colostate.edu.tcp.admin.ConnectionManager;
import cs.colostate.edu.tcp.admin.message.MessageProcessingException;
import cs.colostate.edu.tcp.admin.message.StartClientMessage;
import cs.colostate.edu.tcp.admin.message.SummaryMessage;
import cs.colostate.edu.tcp.admin.message.SummaryRequestMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
            for (int i = 0; i < workers.length; i++) {
                StartClientMessage startClientMessage =
                        new StartClientMessage(getOtherHosts(workers, i), workerPort, numberOfMsgs, numberOfWorkers, clientBuffer);
                connectionManager.sendMessage(startClientMessage, workers[i], adminPort);
            }
        } catch (MessageProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ConnectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.print("\n>>");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String commandString = bufferRead.readLine();
            displaySummary(workers, adminPort);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void displaySummary(String[] workers, int adminPort){
        ConnectionManager connectionManager = new ConnectionManager();

        for (String worker : workers){
            SummaryRequestMessage summaryRequestMessage = new SummaryRequestMessage();
            try {
               SummaryMessage summaryMessage =
                       (SummaryMessage) connectionManager.sendMessage(summaryRequestMessage, worker, adminPort);
                System.out.println("--------------" + worker + " summary --------------------");
                System.out.println("Total received - " + summaryMessage.getTotalReceived() + " Total latency - "
                        + summaryMessage.getTotalLatency() + " Total send - " + summaryMessage.getTotalSend() + " Total time " + summaryMessage.getTotalTime());
                System.out.println("Latency - " + summaryMessage.getLatency() + " Throughput - " + summaryMessage.getThroughput());
            } catch (MessageProcessingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ConnectionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }


    }

    private List<String> getOtherHosts(String[] workers, int i) {

        List<String> otherHosts = new ArrayList<String>();
        for (int j = 0; j < workers.length; j++) {
            if (j != i) {
                otherHosts.add(workers[j]);
            }
        }
        return otherHosts;

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
