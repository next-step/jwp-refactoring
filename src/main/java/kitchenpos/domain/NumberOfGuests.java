package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

	@Column
	private int numberOfGuests;

	protected NumberOfGuests() {}

	private NumberOfGuests(int numberOfGuests) {
		validateNonNegative(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	private void validateNonNegative(int number) {
		if (number < 0) {
			throw new IllegalArgumentException("방문 손님 수는 음수일 수 없습니다.");
		}
	}

	public static NumberOfGuests valueOf(int number) {
		return new NumberOfGuests(number);
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}
}
