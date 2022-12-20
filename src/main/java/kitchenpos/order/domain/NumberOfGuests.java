package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
	@Column(name = "number_of_guests", nullable = false)
	private int value;


	protected NumberOfGuests() {
	}

	private NumberOfGuests(int value) {
		this.value = value;
	}

	public static NumberOfGuests from(int value) {
		return new NumberOfGuests(value);
	}

	public int value() {
		return value;
	}
}
