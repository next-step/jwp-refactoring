package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class NumberOfGuests {

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NumberOfGuests that = (NumberOfGuests)o;
		return getNumberOfGuests() == that.getNumberOfGuests();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getNumberOfGuests());
	}
}
