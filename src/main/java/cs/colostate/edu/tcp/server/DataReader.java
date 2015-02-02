package cs.colostate.edu.tcp.server;

import cs.colostate.edu.tcp.Configurator;
import cs.colostate.edu.tcp.exception.MessageProcessingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/19/14
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataReader extends InputStream {

    // these represents the mode that buffer in.
    public static final int BUFFER_READ_MODE = 1;
    public static final int BUFFER_WRITE_MODE = 2;

    private Lock lock;
    private Condition condition;
    private int mode;

    private ByteBuffer byteBuffer;

    Logger logger = Logger.getLogger(DataReader.class.getName());
    private SocketChannel socketChannel;

    public DataReader(SocketChannel socketChannel) {
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.mode = BUFFER_WRITE_MODE;
        this.byteBuffer = ByteBuffer.allocate(Configurator.getInstance().getByteBufferSize());
        this.socketChannel = socketChannel;
    }

    @Override
    public int read() throws IOException {
        this.lock.lock();
        setReadMode();

        try {
            if (!this.byteBuffer.hasRemaining()) {
                try {
                    this.condition.await();
                } catch (InterruptedException e) {
                    //TODO : handle this properly
                }
            }
            setReadMode();
            int value = this.byteBuffer.get() & 0xFF;
            return value;
        } catch (RuntimeException e){
            this.close();
            throw e;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        this.lock.lock();
        setReadMode();

        try {
            if (!this.byteBuffer.hasRemaining()) {
                try {
                    this.condition.await();
                } catch (InterruptedException e) {
                    //TODO : handle this properly
                }
            }
            setReadMode();
            int chunk = Math.min(this.byteBuffer.remaining(), len);
            this.byteBuffer.get(b, off, chunk);
            return chunk;
        } catch (RuntimeException e){
            this.close();
            throw e;
        } finally {
            this.lock.unlock();
        }

    }

    public void readReady(SelectionKey selectionKey) throws MessageProcessingException {
        this.lock.lock();
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            setWriteMode();
            socketChannel.read(this.byteBuffer);
            this.condition.signalAll();

        } catch (IOException e) {
            System.out.println("Can not read data");
            throw new MessageProcessingException("Can not read from channel", e);
        } finally {
            this.lock.unlock();
        }
    }

    private void setWriteMode() {
        if (this.mode == BUFFER_READ_MODE) {
            if (this.byteBuffer.hasRemaining()) {
                this.byteBuffer.compact();
            } else {
                this.byteBuffer.clear();
            }
            this.mode = BUFFER_WRITE_MODE;
        }
    }

    private void setReadMode() {
        if (this.mode == BUFFER_WRITE_MODE) {
            this.byteBuffer.flip();
            this.mode = BUFFER_READ_MODE;
        }
    }

    public void close(){
        try {
            this.socketChannel.shutdownInput();
            super.close();
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, " Can not shut down the socket channel");
        }
    }

}
