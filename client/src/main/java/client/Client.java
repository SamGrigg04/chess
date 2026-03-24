package client;

import java.util.Scanner;

public class Client {

    private final ServerFacade server;

    public Client(int port) {
        this.server = new ServerFacade(port);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            // TODO: handle commands here
        }
    }
}