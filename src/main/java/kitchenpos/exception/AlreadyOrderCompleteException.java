package kitchenpos.exception;

public class AlreadyOrderCompleteException extends IllegalArgumentException {
	public AlreadyOrderCompleteException(String message) {
		super(message);
	}
}
