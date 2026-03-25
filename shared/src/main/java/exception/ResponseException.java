package exception;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
        AlreadyTakenError,
        UnauthorizedError
    }

    public ResponseException(String message) {
        super(message);
    }

    public static void fromHttpStatusCode() {
    }
}
