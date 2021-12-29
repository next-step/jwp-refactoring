package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.domain.Value;

@Embeddable
public class NumberOfGuests extends Value<NumberOfGuests> {
	@Column(name = "number_of_guests", nullable = false)
	private int value;

	protected NumberOfGuests() {
	}

	public static NumberOfGuests from(Integer value) {
		if (value == null || value < 0) {
			throw new IllegalArgumentException("손님 수는 0보다 같거나 커야 합니다.");
		}

		NumberOfGuests numberOfGuests = new NumberOfGuests();
		numberOfGuests.value = value;
		return numberOfGuests;
	}

	public int getValue() {
		return value;
	}
}
