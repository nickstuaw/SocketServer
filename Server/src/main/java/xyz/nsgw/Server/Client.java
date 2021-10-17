package xyz.nsgw.Server;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Logger;

public class Client extends Thread {

    private final Logger log = Main.getLogger();

    private final Socket socket;
    private final LineReader lineReader;
    private BufferedWriter output;

    public Client(final Socket s, Terminal t) {
        this.socket = s;
        lineReader = LineReaderBuilder.builder()
                        .terminal(t)
                        .build();
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            output = new BufferedWriter(new PrintWriter(socket.getOutputStream(),true));

            while(!Server.isTerminating()) {
                if(Server.isTerminating()) {
                    Server.disconnect(this);
                    break;
                }
                String outputString = input.readLine();

                if(outputString == null) {
                    Server.disconnect(this);
                    break;
                }

                lineReader.printAbove(outputString.strip());

                if(outputString.equals("terminate")) {
                    Server.end();
                    Server.disconnect(this);
                    break;
                }

                Server.sendToAll(outputString);
            }

            input.close();

            output.close();

            socket.close();

        } catch (Exception e) {
            System.out.println("Error occured " +e.getStackTrace());
            e.printStackTrace();
        }
    }

    public SocketAddress address() {
        return this.socket.getLocalSocketAddress();
    }

    public void print(String s) {
        try {
            output.write(s);
            output.newLine();
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
