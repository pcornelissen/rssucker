package net.rssucker;

public class InvalidFeedException extends RuntimeException {
    public InvalidFeedException(Throwable exception) {
        super(exception);
    }
}
