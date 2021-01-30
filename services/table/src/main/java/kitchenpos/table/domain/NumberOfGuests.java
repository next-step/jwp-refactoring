package kitchenpos.table.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
	private static final int MIN_NUMBER_OF_GUESTS = 0;

	private int numberOfGuests;

	protected NumberOfGuests() {
	}

	public NumberOfGuests(int numberOfGuests) {
		if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
			throw new IllegalArgumentException("방문한 손님 수는 " + MIN_NUMBER_OF_GUESTS + " 보다 작을 수 없습니다.");
		}
		this.numberOfGuests = numberOfGuests;
	}

	public void changeValue(int numberOfGuests, boolean empty) {
		if (empty) {
			throw new IllegalArgumentException("빈 테이블은 방문한 손님 수를 수정할 수 없습니다.");
		}
		this.numberOfGuests = numberOfGuests;
	}

	public int getValue() {
		return numberOfGuests;
	}
}
