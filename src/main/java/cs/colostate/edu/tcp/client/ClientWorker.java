package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.Node;
import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.message.Message;
import cs.colostate.edu.tcp.message.TestMessage;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/20/15
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientWorker implements Runnable {

    private Stream stream;
    private long numOfMessages;
    private int clientBuffer;
    private CyclicBarrier cyclicBarrier;
    private CountDownLatch countDownLatch;

    public ClientWorker(Stream stream,
                        long numOfMessages,
                        int clientBuffer,
                        CyclicBarrier cyclicBarrier,
                        CountDownLatch countDownLatch) {
        this.stream = stream;
        this.numOfMessages = numOfMessages;
        this.cyclicBarrier = cyclicBarrier;
        this.countDownLatch = countDownLatch;
        this.clientBuffer = clientBuffer;
    }


    public void run() {

        try {

            try {
                this.cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (BrokenBarrierException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            for (int i = 0; i < this.numOfMessages / this.clientBuffer; i++) {
                List<Message> messages = new ArrayList<Message>(this.clientBuffer);
                for (int j = 0; j < this.clientBuffer; j++) {
                    TestMessage testMessage = new TestMessage(5, System.currentTimeMillis(), 34568, "ecg1", "receiver", "producer");
                    messages.add(testMessage);
                }

                this.stream.emit(messages);

            }
            this.countDownLatch.countDown();

            // these messages are to flush messages of other remaining queues.
            for (int i = 0; i < 50000; i++) {
                List<Message> messages = new ArrayList<Message>(this.clientBuffer);
                for (int j = 0; j < this.clientBuffer; j++) {
                    TestMessage testMessage = new TestMessage(5, System.currentTimeMillis(), 34568, "ecg1", "receiver", "producer");
                    messages.add(testMessage);
                }
                this.stream.emit(messages);
            }

        } catch (MessageProcessingException e) {
            e.printStackTrace();
        }


    }
}
