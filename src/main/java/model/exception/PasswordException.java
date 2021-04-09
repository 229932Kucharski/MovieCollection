package model.exception;

public class PasswordException extends IllegalArgumentException{

    public PasswordException() {
    }

    public PasswordException(String s) {
        super(s);
    }

    public PasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordException(Throwable cause) {
        super(cause);
    }
}
