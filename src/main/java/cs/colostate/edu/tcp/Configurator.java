package cs.colostate.edu.tcp;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 1/20/15
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class Configurator {

    private int ioThreads;
    // number of server tasks
    private int workerPoolSize;

    private int tcpConnections;
    private int bufferSize;

    private int taskBufferMsgs;

    private static Configurator configurator = new Configurator();

    private Configurator() {

    }
    public static Configurator getInstance(){
        return configurator;
    }

    public void setValues(int ioThreads, int workerPoolSize, int tcpConnections, int bufferSize, int taskBufferMsgs){
        this.ioThreads = ioThreads;
        this.workerPoolSize = workerPoolSize;
        this.tcpConnections = tcpConnections;
        this.bufferSize = bufferSize;
        this.taskBufferMsgs = taskBufferMsgs;
    }

    public int getIoThreads() {
        return this.ioThreads;
    }

    public int getWorkerPoolSize() {
        return this.workerPoolSize;
    }

    public int getTcpConnections() {
        return this.tcpConnections;
    }

    public int getByteBufferSize() {
        return this.bufferSize;
    }

    public int getTaskBufferMessages(){
        return  this.taskBufferMsgs;
    }

}
