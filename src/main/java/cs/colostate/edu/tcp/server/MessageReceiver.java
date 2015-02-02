package cs.colostate.edu.tcp.server;

import cs.colostate.edu.tcp.message.TestMessage;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/2/15
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageReceiver {

    private AtomicLong atomicLong = new AtomicLong(0);
    private long lastTime;

    public void onMessage(TestMessage message){

        long currentValue = this.atomicLong.incrementAndGet();
        if ((currentValue % 2000000) == 0) {
            System.out.println("Message Rate ==> " + 2000000000 / (System.currentTimeMillis() - this.lastTime));
            this.lastTime = System.currentTimeMillis();
        }

    }
}
