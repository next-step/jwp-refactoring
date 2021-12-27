package kitchenpos.common;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class Price {

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
		return price.compareTo(BigDecimal.ZERO) < 0;
	}

	public BigDecimal value() {
		return BigDecimal.valueOf(price.intValue());
	}

	public boolean greaterThan(BigDecimal sum) {
		return price.compareTo(sum) > 0;
	}
}
