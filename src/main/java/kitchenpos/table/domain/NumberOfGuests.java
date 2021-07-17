package kitchenpos.table.domain;

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
}
