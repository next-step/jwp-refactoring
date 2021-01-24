package kitchenpos.ordertable.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Guest {
	public static final int GUEST_MINIMUM_COUNT = 0;
	private int numberOfGuests;

	protected Guest() {
	}

	private Guest(final int numberOfGuests) {
		validateNumberOfGuests(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	public static Guest of(final int numberOfGuests) {
		return new Guest(numberOfGuests);
	}

	private void validateNumberOfGuests(final int numberOfGuests) {
		if (numberOfGuests < GUEST_MINIMUM_COUNT) {
			throw new IllegalArgumentException("손님은 0명 이상이여야 합니다.");
		}
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}
