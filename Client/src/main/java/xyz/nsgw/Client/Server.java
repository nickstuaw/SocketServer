package xyz.nsgw.Client;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Server {

    private static boolean terminating = false;

    public Server(String ip, int port) throws IOException {

        Terminal terminal;
        LineReader lineReader;
        terminal = TerminalBuilder.terminal();
        lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();

        String newIp = lineReader.readLine("IP (leave blank for localhost): ");
        if(!newIp.isEmpty()) {
            ip = newIp;
        }
        String newPort = lineReader.readLine("Port (leave blank for 39999): ");
        if(!newPort.isEmpty()) {
            port = Integer.parseInt(newPort);
        }
        Socket s;
        try {
            s = new Socket(ip, port);
        } catch(ConnectException ex) {
            lineReader.printAbove("Connection refused.");
            ex.printStackTrace();
            return;
        }

        try {

            String alias = lineReader.readLine("Alias: ");

            if(alias.isEmpty()) alias = "User";

            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter sOut = new BufferedWriter(new PrintWriter(s.getOutputStream(), true));

            String str = "";

            ClientRunnable clientRun = new ClientRunnable(sIn, lineReader);

            new Thread(clientRun).start();

            lineReader.printAbove("Connected to localhost:25590.");

            while (!(str.equals("stop") || terminating)) {
                str = lineReader.readLine("> ");
                sOut.write(alias + ": " + str);
                sOut.newLine();
                sOut.flush();
            }

            sOut.close();

            sIn.close();

            s.close();

        } catch (UserInterruptException exit) {

            s.close();

        }

    }

    public static void end() {
        terminating = true;
    }

}
