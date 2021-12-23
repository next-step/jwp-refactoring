package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.ordertable.exception.InvalidNumberOfGuestsException;

@Embeddable
public class NumberOfGuests {

	public static final int MIN_INCLUSIVE = 0;

	@Column(name = "number_of_guests", nullable = false)
	private int numberOfGuests;

	protected NumberOfGuests() {
	}

	private NumberOfGuests(int numberOfGuests) {
		validate(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	public static NumberOfGuests of(int numberOfGuests) {
		return new NumberOfGuests(numberOfGuests);
	}

	private static void validate(int numberOfGuests) {
		if (numberOfGuests < MIN_INCLUSIVE) {
			throw new InvalidNumberOfGuestsException();
		}
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		validate(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}
}
