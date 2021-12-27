package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Embeddable
public class Price {

	private static final BigDecimal MINIMUM = BigDecimal.ZERO;

	private BigDecimal price;

	protected Price() {
	}

	private Price(BigDecimal price) {
		this.price = price;
	}

	public static Price of(BigDecimal price) {
		validate(price);
		return new Price(price);
	}

	public BigDecimal toBigDecimal() {
		return price;
	}

	public static void validate(BigDecimal price) {
		if (Objects.isNull(price)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "가격은 Null 일 수 없습니다");
		}
		if (price.compareTo(BigDecimal.ZERO) < 0) {
			throw new AppException(ErrorCode.WRONG_INPUT, "가격은 최소값({}) 이상이어야 합니다", MINIMUM);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Price price1 = (Price)o;

		return price.equals(price1.price);
	}

	@Override
	public int hashCode() {
		return price.hashCode();
	}
}
