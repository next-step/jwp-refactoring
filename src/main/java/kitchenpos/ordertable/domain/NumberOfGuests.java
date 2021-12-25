package kitchenpos.ordertable.domain;

import java.util.Objects;

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

	public void changeNumberOfGuests(int numberOfGuests) {
		validate(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof NumberOfGuests)) {
			return false;
		}
		NumberOfGuests that = (NumberOfGuests)o;
		return numberOfGuests == that.numberOfGuests;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberOfGuests);
	}
}
