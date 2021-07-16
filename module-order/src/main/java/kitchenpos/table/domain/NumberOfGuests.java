package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.table.exception.TableException;

@Embeddable
public class NumberOfGuests {

	private static final int MIN_GUEST = 0;

	@Column(name = "number_of_guests", length = 11, nullable = false)
	private int number;

	protected NumberOfGuests() {

	}

	public NumberOfGuests(int number) {
		validate(number);
		this.number = number;
	}

	private void validate(int number) {
		if (number < MIN_GUEST) {
			throw new TableException("주문 테이블의 인원은 0보다 작을 수 없습니다");
		}
	}

	public int value() {
		return this.number;
	}
}
