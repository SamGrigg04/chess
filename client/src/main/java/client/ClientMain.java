package client;

public class ClientMain {
    public static void main(String[] args) {
        // Reads an optional server URL
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        // Starts the client (command line thingy)
        try {
            new Client(serverUrl).run();
        } catch (Exception ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }

}
