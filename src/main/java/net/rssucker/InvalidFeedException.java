package net.rssucker;

class InvalidFeedException extends RuntimeException {
	public InvalidFeedException(Throwable exception) {
		super(exception);
	}
}
