package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
	private static final int ZERO = 0;

	@Column(nullable = false)
	private int numberOfGuests;

	public NumberOfGuests() {
	}

	public NumberOfGuests(int numberOfGuests) {
		validateNumberOfGuestNotMinus(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	private void validateNumberOfGuestNotMinus(int numberOfGuests) {
		if (numberOfGuests < ZERO) {
			throw new IllegalArgumentException();
		}
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NumberOfGuests that = (NumberOfGuests)o;
		return numberOfGuests == that.numberOfGuests;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberOfGuests);
	}
}
