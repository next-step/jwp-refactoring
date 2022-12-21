package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.Assert;

@Embeddable
public class NumberOfGuests {
	@Column(name = "number_of_guests", nullable = false)
	private int value;


	protected NumberOfGuests() {
	}

	private NumberOfGuests(int value) {
		validtae(value);
		this.value = value;
	}

	private static void validtae(int value) {
		Assert.isTrue(graterThanOrEqualToZero(value), String.format("인원 수(%d)는 반드시 0이상 이어야 합니다.", value));
	}

	private static boolean graterThanOrEqualToZero(int value) {
		return value >= 0;
	}

	public static NumberOfGuests from(int value) {
		return new NumberOfGuests(value);
	}

	public int value() {
		return value;
	}
}
