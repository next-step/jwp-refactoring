package kitchenpos.menu.application;


import common.application.ValidationException;

public class MenuValidationException extends ValidationException {

	public MenuValidationException() {
	}

	public MenuValidationException(String message) {
		super(message);
	}

	public MenuValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public MenuValidationException(Throwable cause) {
		super(cause);
	}

	public MenuValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
