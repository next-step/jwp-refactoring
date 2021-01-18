package kitchenpos.common;

public class TableValidationException extends ValidationException {
	public TableValidationException() {
	}

	public TableValidationException(String message) {
		super(message);
	}

	public TableValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TableValidationException(Throwable cause) {
		super(cause);
	}

	public TableValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
