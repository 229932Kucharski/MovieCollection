package model.exception;

public class EmailException extends IllegalArgumentException{
    public EmailException() {
    }

    public EmailException(String s) {
        super(s);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailException(Throwable cause) {
        super(cause);
    }
}
