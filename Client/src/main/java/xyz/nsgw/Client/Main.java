package xyz.nsgw.Client;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        String ip = "localhost";
        int port = 39999;

        try {

            new Server(ip, port);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
