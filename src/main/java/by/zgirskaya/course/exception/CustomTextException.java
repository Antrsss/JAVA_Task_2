package by.zgirskaya.course.exception;

public class CustomTextException extends Exception {

    public CustomTextException() {
        super();
    }

    public CustomTextException(String message) {
        super(message);
    }

    public CustomTextException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomTextException(Throwable cause) {
        super(cause);
    }
}
