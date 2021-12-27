package kitchenpos.common;

import java.math.BigDecimal;

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
		return new Price(price);
	}

	public boolean lessThanZero() {
		return price.compareTo(BigDecimal.ZERO) == LESS_THAN_COMPARE_VALUE;
	}

	public BigDecimal value() {
		return BigDecimal.valueOf(price.intValue());
	}

	public boolean greaterThan(Price sum) {
		return price.compareTo(sum.price) == GREATER_THAN_COMPARE_VALUE;
	}
}
