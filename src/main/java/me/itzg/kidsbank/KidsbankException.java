package me.itzg.kidsbank;

public class KidsbankException extends Exception {
	private static final long serialVersionUID = 1L;

	public KidsbankException() {
	}

	public KidsbankException(String message) {
		super(message);
	}

	public KidsbankException(Throwable cause) {
		super(cause);
	}

	public KidsbankException(String message, Throwable cause) {
		super(message, cause);
	}

	public KidsbankException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
