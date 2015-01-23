package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Configurator;
import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.message.TestMessage;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/20/15
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    public void startClient(String host, int port, int numOfMessages, int numberOfWorkers, int clientBuffer) {
        ClientManager clientManager = new ClientManager();
        clientManager.start();

        Node targetNode = new Node(port, host);

        for (int i = 0; i < 20; i++) {
            TestMessage testMessage = new TestMessage(5, System.currentTimeMillis(), 34568, "ecg1", "receiver", "producer");
            try {
                clientManager.sendEvent(testMessage, targetNode);
            } catch (MessageProcessingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfWorkers + 1);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfWorkers);
        for (int i = 0; i < numberOfWorkers; i++) {
            ClientWorker clientWorker = new ClientWorker(clientManager, targetNode,
                    numOfMessages / numberOfWorkers, clientBuffer, cyclicBarrier, countDownLatch);
            Thread thread = new Thread(clientWorker);
            thread.start();
        }

        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BrokenBarrierException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        long startTime = System.currentTimeMillis();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("Throughput ==> " + numOfMessages * 1000.0 / (System.currentTimeMillis() - startTime));

    }

    public static void main(String[] args) {

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int numberOfMsgs = Integer.parseInt(args[2]);
        int numberOfWorkers = Integer.parseInt(args[3]);

        int ioThreads = Integer.parseInt(args[4]);
        int connections = Integer.parseInt(args[5]);
        int bufferSize = Integer.parseInt(args[6]);

        int clientBuffer = Integer.parseInt(args[7]);

        Configurator configurator = Configurator.getInstance();
        configurator.setValues(ioThreads, 20, connections, bufferSize, 20);

        Client client = new Client();
        client.startClient(host, port, numberOfMsgs, numberOfWorkers, clientBuffer);

    }


}
























