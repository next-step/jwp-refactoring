package kitchenpos.exception;

public class WrongPriceException extends IllegalArgumentException {
	public WrongPriceException(String message) {
		super(message);
	}
}
