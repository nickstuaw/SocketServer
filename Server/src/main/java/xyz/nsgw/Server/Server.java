package xyz.nsgw.Server;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Server extends Thread {

    private static ServerSocket SERVER_SOCKET;
    private static volatile boolean terminating = false;
    private static Logger log;
    private static final ArrayList<Client> threadList = new ArrayList<>();
    private final Terminal terminal;

    public Server(LineReader lineReader) throws IOException {

        this.terminal = TerminalBuilder.terminal();

        lineReader.printAbove("Starting up...");

        SERVER_SOCKET = new ServerSocket(25590);

        this.start();

        log = Logger.getLogger("nicks_server");

        lineReader.printAbove("Online.");

        InputRunnable inputRunnable = new InputRunnable(lineReader);

        new Thread(inputRunnable).start();

        while (!terminating) {
            Thread.onSpinWait();
        }
        Thread.currentThread().interrupt();

        SERVER_SOCKET.close();

        log.info("Server closed.");

        System.exit(0);
    }

    @Override
    public void run() {

        try {

            Client client;

            while (!terminating) {

                try {

                    Socket socket = SERVER_SOCKET.accept();

                    if (threadList.stream().anyMatch(s -> s.address() == socket.getLocalSocketAddress())) continue;

                    client = new Client(socket, terminal);

                    threadList.add(client);

                    new Thread(client).start();

                } catch (SocketException ignored) {}

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(Client thread) {
        threadList.remove(thread);
    }

    public static void sendToAll(String outputString) throws IOException {
        threadList.forEach(c -> c.print(outputString));
    }

    public static void end() {
        log.info("Stopping the server...");
        terminating = true;
    }

    public static boolean isTerminating() {
        return terminating;
    }
}
