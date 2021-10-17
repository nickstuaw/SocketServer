package xyz.nsgw.Server;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Main {

    private static boolean running = true;

    private static final ArrayList<Client> threadList = new ArrayList<>();

    private static Server server;

    public static void main(String[] args) {

        try {

            Terminal terminal = TerminalBuilder.terminal();
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            server = new Server(lineReader);

        } catch (Exception e) {
            System.out.println("Error occurred in main: " + e.getStackTrace());
        }

    }

    public static Logger getLogger() {
        return Logger.getLogger("nicks_server");
    }

}
