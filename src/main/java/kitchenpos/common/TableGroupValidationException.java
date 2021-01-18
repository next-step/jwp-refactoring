package kitchenpos.common;

public class TableGroupValidationException extends ValidationException {
	public TableGroupValidationException() {
	}

	public TableGroupValidationException(String message) {
		super(message);
	}

	public TableGroupValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TableGroupValidationException(Throwable cause) {
		super(cause);
	}

	public TableGroupValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
