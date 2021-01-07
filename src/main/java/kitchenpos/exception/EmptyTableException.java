package kitchenpos.exception;

public class EmptyTableException extends IllegalArgumentException {
	public EmptyTableException(String message) {
		super(message);
	}
}
