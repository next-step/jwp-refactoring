package kitchenpos.common;

public class InvalidQuantityException extends IllegalArgumentException {

	public static final String MESSAGE = String.format("수령은 %d과 같거나 커야합니다", Quantity.MIN_INCLUSIVE);

	public InvalidQuantityException() {
		super(MESSAGE);
	}
}
