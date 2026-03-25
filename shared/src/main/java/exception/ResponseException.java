package exception;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
        AlreadyTakenError,
        UnauthorizedError
    }

    final private Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code code() {
        return code;
    }

    public static Code fromHttpStatusCode(int httpStatusCode) {
        return switch (httpStatusCode) {
            case 500 -> Code.ServerError;
            case 400 -> Code.ClientError;
            case 403 -> Code.AlreadyTakenError;
            case 401 -> Code.UnauthorizedError;
            default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
        };
    }
}
