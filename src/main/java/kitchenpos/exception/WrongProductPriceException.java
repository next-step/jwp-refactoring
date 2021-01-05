package kitchenpos.exception;

public class WrongProductPriceException extends IllegalArgumentException {
	public WrongProductPriceException(String message) {
		super(message);
	}
}
