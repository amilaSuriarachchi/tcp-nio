package cs.colostate.edu.tcp.client;

import cs.colostate.edu.tcp.exception.MessageProcessingException;
import cs.colostate.edu.tcp.Configurator;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/17/14
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataWritter extends OutputStream {

    public static final int BUFFER_READ_MODE = 1;  // read the data from buffer and write to channel
    public static final int BUFFER_WRITE_MODE = 2;

    private ByteBuffer byteBuffer;
    private Lock lock;
    private Condition condition;
    private int mode;
    private ClientConnection clientConnection;

    private boolean isClosed;

    public DataWritter(ClientConnection clientConnection) {
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.byteBuffer = ByteBuffer.allocate(Configurator.getInstance().getByteBufferSize());
        this.mode = BUFFER_WRITE_MODE;
        this.clientConnection = clientConnection;
        this.isClosed = false;
    }

    public void writeReady(SelectionKey selectionKey) throws MessageProcessingException {
        this.lock.lock();
        try {
            setReadMode();
            // this is to improve the performance if one connection is being used.
            if (this.byteBuffer.hasRemaining()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                socketChannel.write(this.byteBuffer);
                this.condition.signalAll();
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write to the channel " + e.getMessage(), e);
        } finally {
            this.lock.unlock();
        }

    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b});
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // this method suppose to write until whole bytes written to the buffer
        this.lock.lock();
        setWriteMode();
        try {
            while (len > 0) {
                if (this.isClosed){
                    throw new IOException("Data writer is closed ");
                }
                if (!this.byteBuffer.hasRemaining()) {
                    try {
                        this.condition.await();
                    } catch (InterruptedException e) {
                        //TODO: handle this properly
                    }
                    setWriteMode();
                }

                int chunk = Math.min(this.byteBuffer.remaining(), len);
                this.byteBuffer.put(b, off, chunk);
                off += chunk;
                len -= chunk;
            }
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

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void close() throws IOException {
        this.lock.lock();
        this.isClosed = true;
        this.condition.signalAll();
        this.lock.unlock();
        super.close();
    }
}
