import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.create(80);
        server.addFile(new File("WebCode/HTML/index.html"), "/");
        server.start();
    }
}
