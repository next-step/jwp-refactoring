package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
	@Column(name = "number_of_guests")
	private int count;

	protected NumberOfGuests() {
	}

	public NumberOfGuests(int count) {
		if (count < 0) {
			throw new IllegalArgumentException("방문 손님 수는 0보다 작을 수 없습니다.");
		}
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NumberOfGuests that = (NumberOfGuests)o;
		return count == that.count;
	}

	@Override
	public int hashCode() {
		return Objects.hash(count);
	}
}
