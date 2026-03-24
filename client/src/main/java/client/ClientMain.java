package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        int port = 8080; // or whatever your server runs on

        var client = new Client(port);
        client.run();
    }
}
