package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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

	public int getValue() {
		return numberOfGuests;
	}
}
