package kitchenpos.common.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Embeddable
public class Quantity {

	private static final Long MINIMUM = 1L;

	private Long quantity;

	public Quantity() {
	}

	private Quantity(Long quantity) {
		this.quantity = quantity;
	}

	public static Quantity of(Long quantity) {
		validate(quantity);
		return new Quantity(quantity);
	}

	private static void validate(Long quantity) {
		if (Objects.isNull(quantity)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "수량은 Null 일 수 없습니다");
		}
		if (quantity < MINIMUM) {
			throw new AppException(ErrorCode.WRONG_INPUT, "최소 수량({}) 이상이어야 합니다", MINIMUM);
		}
	}

	public static Quantity valueOf(Long quantity) {
		return of(quantity);
	}

	public Long toLong() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Quantity quantity1 = (Quantity)o;

		return quantity.equals(quantity1.quantity);
	}

	@Override
	public int hashCode() {
		return quantity.hashCode();
	}
}
