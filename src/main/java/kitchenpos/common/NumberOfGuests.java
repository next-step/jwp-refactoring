package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {

	static final String MSG_VALIDATE_GUESTS = "numberOfGuests must be equal or greater than zero";

	@Column(name = "numberOfGuests", nullable = false)
	private final int numberOfGuests;

	protected NumberOfGuests() {
		this(0);
	}

	public NumberOfGuests(int numberOfGuests) {
		validate(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	private void validate(int numberOfGuests) {
		if (numberOfGuests < 0) {
			throw new ValidationException(MSG_VALIDATE_GUESTS);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NumberOfGuests)) return false;
		NumberOfGuests that = (NumberOfGuests) o;
		return numberOfGuests == that.numberOfGuests;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numberOfGuests);
	}

	public int getValue() {
		return numberOfGuests;
	}
}
