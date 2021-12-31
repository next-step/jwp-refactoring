package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Embeddable
public class NumberOfGuests {

	private static final int MINIMUM = 0;

	private Integer numberOfGuests;

	protected NumberOfGuests() {
	}

	private NumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public static NumberOfGuests of(Integer numberOfGuests) {
		validate(numberOfGuests);
		return new NumberOfGuests(numberOfGuests);
	}

	private static void validate(Integer quantity) {
		if (Objects.isNull(quantity)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "인원은 Null 일 수 없습니다");
		}
		if (quantity < MINIMUM) {
			throw new AppException(ErrorCode.WRONG_INPUT, "최소 인원 수({}) 이상이어야 합니다", MINIMUM);
		}
	}

	public int toInt() {
		return numberOfGuests;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		NumberOfGuests that = (NumberOfGuests)o;

		return numberOfGuests.equals(that.numberOfGuests);
	}

	@Override
	public int hashCode() {
		return numberOfGuests.hashCode();
	}
}
