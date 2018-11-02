package cs425.mp3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * All operations regarding distributed FS
 */
public final class FileOperation {
    private final Logger logger = LoggerFactory.getLogger(FileOperation.class);
    private static final int bufSize = Config.FILE_BUFFER_SIZE;

    // Runtime variable
    private final Node node;
    private final ExecutorService exec;
    private final String serverHostname;
    private final ServerSocket serverSocket;
    private boolean isFileServerRunning;

    // File meta data
    private ConcurrentHashMap<String, Set<String>> localFileMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Set<String>> sdfsFileMap = new ConcurrentHashMap<>();

    public FileOperation(Node n) throws IOException {
        this.node = n;
        this.serverHostname = InetAddress.getLocalHost().getCanonicalHostName();
        this.serverSocket = new ServerSocket(Config.TCP_PORT);
        int nThreads = Config.NUM_CORES * 2;
        this.exec = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            this.exec.submit(this.mainFileServer());
        }
        this.isFileServerRunning = true;
    }

    public void stopServer() {
        this.isFileServerRunning = false;
    }

    public void put(String localFileName, String sdfsFileName) {

    }

    public void get(String sdfsFileName, String localFileName) {

    }

    public void delete(String sdfsFileName) {

    }

    public void listFileLocations(String sdfsFileName) {

    }

    public void listFileLocal() {

    }

    public void getVersions(String sdfsFileName, String numVersions, String localFileName) {
        int numOfLatestVersions;
        try {
            numOfLatestVersions = Integer.valueOf(numVersions);
        } catch (NumberFormatException n) {
            logger.error("Version number input error");
            numOfLatestVersions = 1;
        }
    }

    /**
     * Just send the file via socket, do nothing with socket
     */
    private void sendFileViaSocket(String filePath, Socket socket) throws IOException {
        socket.setSoTimeout(120_000); // 120s timeout
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            bufferedReadWrite(in, out);
            logger.info("Finished sending file");
        }
    }

    /**
     * Receive a file via socket, do nothing with socket
     */
    private void readFileViaSocket(String targetPath, Socket socket) throws IOException {
        socket.setSoTimeout(120_000); // 120s timeout
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetPath))) {
            bufferedReadWrite(socket.getInputStream(), bos);
            logger.info("Finished receiving file");
        }
    }

    private void bufferedReadWrite(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[bufSize];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.flush();
    }

    /**
     * Define operations for the file server
     */
    private Runnable mainFileServer() {
        return () -> {
            Thread.currentThread().setName("FS-loop");
            logger.info("File server running: <{}>", this.serverHostname);
            while (this.isFileServerRunning) {
                Socket clientSocket;
                try {
                    clientSocket = this.serverSocket.accept();
                    logger.info("Connection from client {}.", clientSocket.getRemoteSocketAddress());
                } catch (IOException e) {
                    logger.error("Server socket failed", e);
                    continue;
                }
                // Logic below
            }
            logger.info("File server stopped: <{}>", this.serverHostname);
        };
    }

}
