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

    private int totoalSend;
    private long totalTime;

    public void initializeConnections(Stream stream) {
        List<Message> testMessages = new ArrayList<Message>(20);
        for (int i = 0; i < 20; i++) {
            TestMessage testMessage = new TestMessage(5, System.currentTimeMillis(), 34568, "ecg1", "receiver", "producer");
            testMessages.add(testMessage);
        }
        try {
            stream.emit(testMessages);
        } catch (MessageProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void startClient(Stream stream, int numOfMessages, int numberOfWorkers, int clientBuffer) {


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

    public int getTotoalSend() {
        return totoalSend;
    }

    public long getTotalTime() {
        return totalTime;
    }

    //    public static void main(String[] args) {
//
//        String host = args[0];
//        int port = Integer.parseInt(args[1]);
//        int numberOfMsgs = Integer.parseInt(args[2]);
//        int numberOfWorkers = Integer.parseInt(args[3]);
//
//        int ioThreads = Integer.parseInt(args[4]);
//        int connections = Integer.parseInt(args[5]);
//        int bufferSize = Integer.parseInt(args[6]);
//
//        int clientBuffer = Integer.parseInt(args[7]);
//
//        Configurator configurator = Configurator.getInstance();
//        configurator.setValues(ioThreads, 20, connections, bufferSize, 20);
//
//        Client client = new Client();
//        client.startClient(host, port, numberOfMsgs, numberOfWorkers, clientBuffer);
//
//    }


}
























