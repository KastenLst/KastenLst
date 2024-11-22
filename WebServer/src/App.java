import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;

public class App {
    public static void main(String[] args) {
        System.out.println("KastenLst WebServer");
        System.out.println("Implementation 1.0");
        System.out.print("Enter Data Path: ");
        File dataFolder = new File(read());
        if (!dataFolder.isDirectory()) {
            System.err.println("Entered Path is not a Directory!");
            System.exit(0);
        } else if (!dataFolder.exists()) {
            System.err.println("Entered Path not exist!");
            System.exit(0);
        }
        File urlsFile = new File(dataFolder, "urls.conf");
        if (urlsFile.isDirectory()) {
            System.err.println("urls.conf is a Directory!");
            System.exit(0);
        } else if (!urlsFile.exists()) {
            System.err.println("urls.conf not exist!");
            System.exit(0);
        }
        try {
            System.out.print("Enter Server Port: ");
            Server server = new Server(Integer.parseInt(read()), 0);
            System.out.println("Loading urls.conf...");
        } catch (BindException be) {
            System.err.println("Failed to start Server!");
            System.err.println("Reason: Failed to Bind Server!");
            System.exit(0);
        } catch (IOException ioe) {
            System.err.println("Failed to start Server!");
            System.err.println("Reason: IO Error occured!");
            System.exit(0);
        } catch (NumberFormatException nfe) {
            System.err.println("Failed to start Server!");
            System.err.println("Reason: Illegal Number Format!");
            System.exit(0);
        } catch (IllegalArgumentException iae) {
            System.err.println("Failed to start Server!");
            System.err.println("Reason: Illegal Arugment!");
            System.exit(0);
        }
    }
    private static String read() {
        try {
            String result = "";
            char c;
            c = (char) System.in.read();
            while (c != '\n') {
                if (c != '\n') {
                    result += c;
                }
            }
            return result.trim();
        } catch (IOException e) {
            System.err.println("Fatal Error");
            System.exit(0);
        }
        return null;
    }
    private String readFile (File file) throws IOException {
        InputStream is = new FileInputStream(file);
        String result = "";
        while (is.available() > 0) {
            result += is.read();
        }
        is.close();
        return result;
    }
}
