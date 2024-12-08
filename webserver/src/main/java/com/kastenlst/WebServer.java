package com.kastenlst; 

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
    private HttpServer httpserver;
    private Map<String, HttpHandler> handlers; 
    public WebServer() throws IOException {
        this.httpserver = HttpServer.create();
        this.handlers = new HashMap<>();
    }
    public void bind(InetSocketAddress address) throws IOException {
        this.httpserver.bind(address, 0);
    }
    public void start() {
        this.httpserver.start();
    }
    public void stop() {
        this.httpserver.stop(1000);
    }
    public void addContext(String path, InputStream source) throws IOException {
        HttpHandler handler = new defaultHandler(source);
        this.httpserver.createContext(path, handler);
        this.handlers.put(path, handler);
    }
    public void addHandler(String path, HttpHandler handler) {
        this.httpserver.createContext(path, handler);
        this.handlers.put(path, handler);
    }
    public HttpHandler getHandler(String path) {
        return this.handlers.get(path);
    }
    public void removeHandler(String path) {
        if (this.handlers.containsKey(path)) {
            this.httpserver.removeContext(path);
            this.handlers.remove(path);
        } else {
            throw new IllegalArgumentException("No Handler for " + path + " exist!");
        }
    }
    private static class defaultHandler implements HttpHandler {
        private final byte[] bytes;
        private final int responseCode;
        public defaultHandler(InputStream source) throws IOException {
            byte[] temporaryBytes;
            int temporaryResponseCode;
            try {
                temporaryBytes = source.readAllBytes();
                source.close();
                temporaryResponseCode = 200;
            } catch (IOException e) {
                temporaryBytes = new byte[] {'E', 'r', 'r', 'o', 'r'};
                temporaryResponseCode = 592;
                throw new IOException(e);
            }
            this.bytes = temporaryBytes;
            this.responseCode = temporaryResponseCode;
        }
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().trim() == "PUT") {
                exchange.sendResponseHeaders(responseCode, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.getResponseBody().close();
            } else {
                exchange.sendResponseHeaders(405, "error".length());
                exchange.getResponseBody().write("error".getBytes());
                exchange.getResponseBody().close();
            }
        }
    }
}