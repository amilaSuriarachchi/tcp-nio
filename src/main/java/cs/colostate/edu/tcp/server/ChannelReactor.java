package cs.colostate.edu.tcp.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/19/14
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChannelReactor implements Runnable {

    private Logger logger = Logger.getLogger(ChannelReactor.class.getName());

    private Queue<SocketChannel> connectionQueue;
    private Selector selector;
    private ServerConnection serverConnection;

    public ChannelReactor(ServerConnection serverConnection) throws IOException {
        this.connectionQueue = new ConcurrentLinkedQueue<SocketChannel>();
        this.selector = Selector.open();
        this.serverConnection = serverConnection;
    }

    public void run() {

        while (this.selector.isOpen()) {
            try {
                processNewChannels();
                this.selector.select();
                for (SelectionKey selectionKey : this.selector.selectedKeys()) {
                    if (selectionKey.isReadable()) {
                        DataReader dataReader = (DataReader) selectionKey.attachment();
                        dataReader.readReady(selectionKey);
                    }
                }
                this.selector.selectedKeys().clear();

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can not process the channel ", e);
            }
        }

    }

    private void processNewChannels() throws IOException {
        SocketChannel socketChannel;
        while ((socketChannel = this.connectionQueue.poll()) != null) {
            // register this channel with this selector
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(this.selector, SelectionKey.OP_READ);
            this.serverConnection.registerSelectionKey(selectionKey);


        }
    }

    public void addNewChannel(SocketChannel socketChannel) {
        this.connectionQueue.add(socketChannel);
        //selector thread may be blocking for new connections. Need to wake up that.
        this.selector.wakeup();
    }
}
