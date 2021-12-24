package kitchenpos.order.exception;

public class InvalidOrderException extends IllegalArgumentException {

	public InvalidOrderException(String message) {
		super(message);
	}
}
