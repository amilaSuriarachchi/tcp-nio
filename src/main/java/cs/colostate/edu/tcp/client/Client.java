package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Configurator;
import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.message.Message;
import cs.colostate.edu.tcp.message.TestMessage;

import java.util.ArrayList;
import java.util.List;
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

    private long totoalSend;
    private long totalTime;

    public void initializeConnections(Stream stream) {
        List<Message> testMessages = new ArrayList<Message>();
        try {
            stream.emit(testMessages);
        } catch (MessageProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void startClient(Stream stream, long numOfMessages, int numberOfWorkers, int clientBuffer) {


        CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfWorkers + 1);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfWorkers);
        for (int i = 0; i < numberOfWorkers; i++) {
            ClientWorker clientWorker = new ClientWorker(stream,
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
        this.totoalSend = numOfMessages;
        this.totalTime = (System.currentTimeMillis() - startTime);

        System.out.println("Throughput ==> " + numOfMessages * 1000.0 / (System.currentTimeMillis() - startTime));

    }

    public long getTotoalSend() {
        return totoalSend;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void startClient(String[] servers, int port, long numberOfMessages, int numberOfWorkers, int clientBuffer) {
        List<Node> nodes = new ArrayList<Node>();
        for (String host : servers) {
            nodes.add(new Node(port, host));
        }
        ClientManager clientManager = new ClientManager();
        clientManager.start();
        Stream stream = new Stream(nodes, clientManager);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) { }

        this.startClient(stream, numberOfMessages, numberOfWorkers, clientBuffer);
    }

    public static void main(String[] args) {

        String[] servers = args[0].split(",");
        int port = Integer.parseInt(args[1]);
        long numberOfMessages = Long.parseLong(args[2]);
        int numberOfWorkers = Integer.parseInt(args[3]);
        int clientBuffer = Integer.parseInt(args[4]);

        Configurator configurator = Configurator.getInstance();
        configurator.setValues(1, 20, 8, 8192, 20);

        Client client = new Client();
        client.startClient(servers, port, numberOfMessages, numberOfWorkers, clientBuffer);

    }


}
























