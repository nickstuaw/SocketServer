package xyz.nsgw.Server;

import org.jline.reader.LineReader;

import java.io.IOException;

public class InputRunnable extends Thread {

    private final LineReader lineReader;

    public InputRunnable(LineReader reader) {
        this.lineReader = reader;
    }

    @Override
    public void run() {
        String str = "";
        while (true) {
            str = lineReader.readLine("> ");
            try {
                if(str.equals("stop")) {
                    Server.sendToAll("end");
                    break;
                }
                Server.sendToAll("Server: " + str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Server.sendToAll("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Server.end();
    }
}
