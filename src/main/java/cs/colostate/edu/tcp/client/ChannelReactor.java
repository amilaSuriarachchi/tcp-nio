package cs.colostate.edu.tcp.client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Channel reactor is a thread with register for read and write events of a set of channels.
 */
public class ChannelReactor implements Runnable {

    private Logger logger = Logger.getLogger(ChannelReactor.class.getName());

    private Queue<SelectionKey> connectionQueue;
    private Selector selector;

    public ChannelReactor() throws IOException {
        this.connectionQueue = new ConcurrentLinkedQueue<SelectionKey>();
        this.selector = Selector.open();

    }

    public void run() {

        while (this.selector.isOpen()) {
            try {
                processNewChannels();
                this.selector.select();
                for (SelectionKey selectionKey : this.selector.selectedKeys()) {
                    // we communicate only for one direction. if other server wants to communicate
                    // that will create a separate connection for that.
                    if (selectionKey.isWritable()) {
                        DataWritter dataWritter = (DataWritter) selectionKey.attachment();
                        dataWritter.writeReady(selectionKey);
                    }
                }
                this.selector.selectedKeys().clear();

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can not process the channel ", e);
            }
        }

    }

    private void processNewChannels() throws IOException {
        SelectionKey selectionKey;
        SocketChannel socketChannel;
        ClientConnection clientConnection;
        while ((selectionKey = this.connectionQueue.poll()) != null) {
            // register this channel with this selector
            socketChannel = (SocketChannel) selectionKey.channel();
            socketChannel.configureBlocking(false);
            clientConnection = (ClientConnection) selectionKey.attachment();
            SelectionKey socketKey =
                    socketChannel.register(this.selector, SelectionKey.OP_WRITE);
            clientConnection.registerSelectionKey(socketKey);
        }
    }

    public void addNewChannel(SelectionKey selectionKey) {
        this.connectionQueue.add(selectionKey);
        //selector thread may be blocking for new connections. Need to wake up that.
        this.selector.wakeup();
    }
}
