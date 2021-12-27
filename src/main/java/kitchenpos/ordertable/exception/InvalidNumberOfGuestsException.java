package kitchenpos.ordertable.exception;

import kitchenpos.ordertable.domain.domain.NumberOfGuests;

public class InvalidNumberOfGuestsException extends IllegalArgumentException {

	public static final String MESSAGE = String.format("손님의 수는 %d과 같거나 커야합니다", NumberOfGuests.MIN_INCLUSIVE);

	public InvalidNumberOfGuestsException() {
		super(MESSAGE);
	}
}
