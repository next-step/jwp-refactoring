package kitchenpos.order.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
	private int numberOfGuests;

	public NumberOfGuests() {
	}

	public NumberOfGuests(int numberOfGuests) {
		update(numberOfGuests);
	}

	public int value() {
		return numberOfGuests;
	}

	public void update(int numberOfGuests) {
		validate(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	private void validate(int numberOfGuests) {
		if (numberOfGuests < 0) {
			throw new IllegalArgumentException("손님의 수는 음수 일 수 없습니다.");
		}
	}
}
