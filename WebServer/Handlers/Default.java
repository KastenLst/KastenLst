package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Default implements HttpHandler {
    private final File targetFile;
    public Default(File file) {
        this.targetFile = file;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream is = new FileInputStream(targetFile);
        byte[] contents = is.readAllBytes();
        is.close();
        exchange.sendResponseHeaders(200, contents.length);
        exchange.getResponseBody().write(contents);
        exchange.getResponseBody().close();
    }
}
