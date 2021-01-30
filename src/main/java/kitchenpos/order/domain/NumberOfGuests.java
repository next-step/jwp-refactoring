package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
	@Column
	private int numberOfGuests;

	protected NumberOfGuests() {
	}

	public NumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
		validate();
	}

	private void validate() {
		if (numberOfGuests < 0) {
			throw new IllegalArgumentException();
		}
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}
