package kitchenpos.order.application;

import kitchenpos.common.application.ValidationException;

public class OrderValidationException extends ValidationException {

	public OrderValidationException() {
	}

	public OrderValidationException(String message) {
		super(message);
	}

	public OrderValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderValidationException(Throwable cause) {
		super(cause);
	}

	public OrderValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
