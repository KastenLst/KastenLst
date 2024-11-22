import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

public class Server {
    private final HttpServer hs;
    /**
     * Create a Server instance which will bind to the specified port.
     * A maximum backlog can also be specified. This is the maximum number of queued incoming connections to allow on the listening socket-
     * Queued TCP connections exceeding this limit may be rejected by the TCP implementation.
     * The Server is acquired from the currently installed HttpServerProvider.
     * @param port The port number.
     * @param backlog The socket backlog. If this value is less than or equal to zero, then a system default value is used.
     * @throws IOException If an I/O error occurs.
     * @throws BindException If the server cannot bind to the requested address, or if the server is already bound.
     * @throws IllegalArgumentException If the port parameter is outside the specified range of valid port values.
     */
    public Server(int port, int backlog) throws IOException {
        this.hs = HttpServer.create(new InetSocketAddress(port), backlog);
        this.hs.setExecutor(null);
    }
    /**
     * Starts this server in a new background thread. The background thread inherits the priority, thread group and context class loader of the caller.
     */
    public void start() {
        this.hs.start();
    }
    /**
     * Stops this server by closing the listening socket and disallowing any new exchanges from being processed.
     * The method will then block until all current exchange handlers have completed or else when approximately <i>delay</i> seconds have elapsed (whichever happens sooner).
     * Then, all open TCP connections are closed, the background thread created by {@link #start()} exits, and the method returns.
     * Once stopped, a {@code Server} cannot be re-used.
     * @param delay The maximum time in seconds to wait until exchanges have finished.
     * @throws IllegalArgumentException If delay is less than zero.
     */
    public void stop(int delay) {
        this.hs.stop(delay);
    }
    /**
     * 
     */
    public void createContext(String path, Handler handler) {
        this.hs.createContext(path, handler);
    }
    /**
     * 
     */
    public void removeContext(String path) {
        this.hs.removeContext(path);
    }
}
