package cs.colostate.edu.tcp.server;

import cs.colostate.edu.tcp.Configurator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/19/14
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerIOReactor implements Runnable {

    Logger logger = Logger.getLogger(ServerIOReactor.class.getName());

    private int port;
    private ServerConnection serverConnection;


    public ServerIOReactor(int port, ServerConnection serverConnection) {
        this.port = port;
        this.serverConnection = serverConnection;
    }

    public void run() {
        ServerSocketChannel serverSocket = null;
        try {

            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(this.port));
            serverSocket.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            List<ChannelReactor> channelReactors = new ArrayList<ChannelReactor>();
            //create channel reactors according to the number of processors
            int numberOfProcessors = Configurator.getInstance().getIoThreads();
            for (int i = 0; i < numberOfProcessors; i++) {
                channelReactors.add(new ChannelReactor(this.serverConnection));
            }
            // start the channel reactors
            for (ChannelReactor channelReactor : channelReactors){
                Thread thread = new Thread(channelReactor);
                thread.start();
            }

            int lastChennelSelected = 0;

            while (selector.isOpen()){
                selector.select();
                for (SelectionKey selectionKey : selector.selectedKeys()){
                    if (selectionKey.isAcceptable()){
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //find a chennel to handover this thread in round robin manner
                        int channelNum = lastChennelSelected % numberOfProcessors;
                        lastChennelSelected++;
                        channelReactors.get(channelNum).addNewChannel(socketChannel);
                        logger.log(Level.INFO, "Accepted a connection from " + socketChannel.getRemoteAddress());
                    }
                }
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can not read from the socket channel", e);
        }

    }
}
