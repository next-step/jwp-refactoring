package kitchenpos.common;

public class InvalidPriceException extends IllegalArgumentException {

	public static final String MESSAGE = String.format("가격은 %.0f원과 같거나 커야합니다", Price.MIN_INCLUSIVE);

	public InvalidPriceException() {
		super(MESSAGE);
	}
}
