package by.zgirskaya.course.exception;

public class CustomTextReaderException extends Exception {

    public CustomTextReaderException() {
        super();
    }

    public CustomTextReaderException(String message) {
        super(message);
    }

    public CustomTextReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomTextReaderException(Throwable cause) {
        super(cause);
    }
}
