import Handlers.Default;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private HttpServer httpserver;
    public Server() {}
    public void create(int port) throws IOException {
        this.httpserver = HttpServer.create(new InetSocketAddress(port), 0);
        this.httpserver.setExecutor(null);
    }
    public void start() {
        this.httpserver.start();
    }
    public void stop() {
        this.httpserver.stop(250);
    }
    public void addFile(File file, String path) {
        this.httpserver.createContext(path, new Default(file));
    }
    public void addHandler(HttpHandler handler, String path) {
        this.httpserver.createContext(path, handler);
    }
    public void removePath(String path) {
        this.httpserver.removeContext(path);
    }
    public InetSocketAddress getAddress() {
        return this.httpserver.getAddress();
    }
}
