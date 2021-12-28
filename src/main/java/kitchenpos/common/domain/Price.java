package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Price {

	public static final int LESS_THAN_COMPARE_VALUE = -1;
	public static final int GREATER_THAN_COMPARE_VALUE = 1;

	private final BigDecimal price;

	protected Price() {
		this.price = BigDecimal.ZERO;
	}

	private Price(final BigDecimal price) {
		this.price = BigDecimal.valueOf(price.longValue());
	}

	public static Price valueOf(final BigDecimal price) {
		validatePrice(price);
		return new Price(price);
	}

	private static void validatePrice(BigDecimal price) {
		if (Objects.isNull(price) || lessThanZero(price)) {
			throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다");
		}
	}

	private static boolean lessThanZero(BigDecimal price) {
		return price.compareTo(BigDecimal.ZERO) == LESS_THAN_COMPARE_VALUE;
	}

	public BigDecimal value() {
		return BigDecimal.valueOf(price.intValue());
	}

	public boolean greaterThan(Price sum) {
		return price.compareTo(sum.price) == GREATER_THAN_COMPARE_VALUE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Price price1 = (Price)o;
		return Objects.equals(price, price1.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(price);
	}
}
