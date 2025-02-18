import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(1234), 0);
        httpServer.setExecutor(null);
        httpServer.createContext("/", new TestHandler());
        httpServer.start();
    }
    public static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            if (!Objects.equals(exchange.getRequestMethod(), "POST")) {
                response = exchange.getRequestMethod() + " is not allowed here!";
            } else {
                response = "Form Submitted!";
                System.out.println(new String(exchange.getRequestBody().readAllBytes()));
            }
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
        }
    }
}