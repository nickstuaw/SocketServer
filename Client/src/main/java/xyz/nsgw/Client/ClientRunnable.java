package xyz.nsgw.Client;

import org.jline.reader.LineReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class ClientRunnable implements Runnable {

    private final BufferedReader input;
    private final LineReader console;

    public ClientRunnable(BufferedReader input, LineReader console) {
        this.input = input;
        this.console = console;
    }

    @Override
    public void run() {
        String response;
        while(true) {
            try {
                response = input.readLine();
                if(response == null) break;
                if(response.isEmpty()) break;
                if(response.equals("end")) break;
                console.printAbove(response.strip());
            } catch (SocketException e) {
                console.printAbove("Socket closed.");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Server.end();
    }
}