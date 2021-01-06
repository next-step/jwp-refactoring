package kitchenpos.exception;

public class AlreadyOrderException extends IllegalArgumentException {
	public AlreadyOrderException(String message) {
		super(message);
	}
}
