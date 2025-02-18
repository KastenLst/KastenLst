import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;


/**
 * Manages user accounts and session handling using Java's native APIs with optional logging.
 */
public class AccountManager {
    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final long SESSION_EXPIRY_MS = TimeUnit.DAYS.toMillis(3); // 3 Days

    private final Logger logger = Logger.getLogger(AccountManager.class.getName());
    private final File accountsFile;
    private final Map<Integer, Session> activeSessions = new HashMap<>();
    private final Random random = new SecureRandom();
    private final Properties properties = new Properties();
    private boolean isFileLoggingEnabled = false;

    private static class Session {
        String userId;
        long creationTime;

        Session(String userId) {
            this.userId = userId;
            this.creationTime = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - creationTime > SESSION_EXPIRY_MS;
        }
    }

    public AccountManager(File accountsDatabaseFile) {
        this.accountsFile = accountsDatabaseFile;
        loadAccounts();
    }

    /**
     * Enables file logging when called. If not called, logging only goes to console.
     */
    public void setupLogger(String logFilePath) {
        try {
            if (isFileLoggingEnabled) {
                logger.warning("Logger is already set up.");
                return;
            }

            if (logFilePath == null || logFilePath.trim().isEmpty()) {
                throw new IllegalArgumentException("Log file path cannot be empty.");
            }

            File logFile = new File(logFilePath);
            logFile.getParentFile().mkdirs(); // Ensure directory exists

            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(true);
            isFileLoggingEnabled = true;
            logger.info("File logging enabled: " + logFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to set up logger", e);
        }
    }

    public String createAccount(String username, String password) throws SecurityException {
        if (properties.containsKey(username + ".userId")) {
            logger.warning("Attempt to create an existing account: " + username);
            throw new SecurityException("User already exists.");
        }
        String userId = UUID.randomUUID().toString();
        String hashedPassword = hashPassword(password);
        properties.setProperty(username + ".userId", userId);
        properties.setProperty(username + ".password", hashedPassword);
        saveAccounts();
        logger.info("New account created: " + username + " (User ID: " + userId + ")");
        return userId;
    }

    public Integer createSession(String username, String password) throws SecurityException {
        if (!properties.containsKey(username + ".userId")) {
            logger.warning("Failed login attempt for non-existing user: " + username);
            throw new SecurityException("User does not exist.");
        }
        String storedHash = properties.getProperty(username + ".password");
        if (!verifyPassword(password, storedHash)) {
            logger.warning("Invalid password attempt for user: " + username);
            throw new SecurityException("Invalid password.");
        }

        String userId = properties.getProperty(username + ".userId");
        int sessionId = random.nextInt(Integer.MAX_VALUE);
        activeSessions.put(sessionId, new Session(userId));
        logger.info("User " + username + " logged in. Session ID: " + sessionId);
        return sessionId;
    }

    public boolean isSessionValid(int sessionId) {
        Session session = activeSessions.get(sessionId);
        if (session == null || session.isExpired()) {
            activeSessions.remove(sessionId);
            logger.warning("Session expired or invalid. Session ID: " + sessionId);
            return false;
        }
        return true;
    }

    public String getUserIdFromSession(int sessionId) throws SecurityException {
        Session session = activeSessions.get(sessionId);
        if (session == null || session.isExpired()) {
            activeSessions.remove(sessionId);
            logger.warning("Attempted access with expired session: " + sessionId);
            throw new SecurityException("Session is invalid or expired.");
        }
        return session.userId;
    }

    public String getUserID(String username, String password) throws SecurityException {
        if (!properties.containsKey(username + ".userId")) {
            logger.warning("Attempted ID retrieval for non-existing user: " + username);
            throw new SecurityException("User does not exist.");
        }
        String storedHash = properties.getProperty(username + ".password");
        if (!verifyPassword(password, storedHash)) {
            logger.warning("Failed ID retrieval due to incorrect password for user: " + username);
            throw new SecurityException("Invalid password.");
        }
        return properties.getProperty(username + ".userId");
    }

    public void logout(int sessionId) throws SecurityException {
        if (!activeSessions.containsKey(sessionId)) {
            logger.warning("Attempted logout with invalid session ID: " + sessionId);
            throw new SecurityException("Session does not exist.");
        }
        activeSessions.remove(sessionId);
        logger.info("Session ended. Session ID: " + sessionId);
    }

    private String hashPassword(String password) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private boolean verifyPassword(String password, String storedHash) {
        try {
            String[] parts = storedHash.split("\\$");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            byte[] testHash = factory.generateSecret(spec).getEncoded();

            return Arrays.equals(hash, testHash);
        } catch (Exception e) {
            return false;
        }
    }

    private void loadAccounts() {
        if (accountsFile.exists()) {
            try (FileInputStream fis = new FileInputStream(accountsFile)) {
                properties.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Error loading accounts file", e);
            }
        }
    }

    private void saveAccounts() {
        try (FileOutputStream fos = new FileOutputStream(accountsFile)) {
            properties.store(fos, "User Account Data");
        } catch (IOException e) {
            throw new RuntimeException("Error saving accounts file", e);
        }
    }

    public static void main(String[] args) {
        File dbFile = new File("accounts.properties");

        // Create account manager (doesn't log to file initially)
        AccountManager manager = new AccountManager(dbFile);

        // Enable logging to a file later
        manager.setupLogger("logs/my_custom_log.log");

        try {
            String userId = manager.createAccount("testUser", "securePassword123");
            System.out.println("User created! User ID: " + userId);
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
