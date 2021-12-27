package kitchenpos.common;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class Price {

	private final BigDecimal price;

	public Price() {
		this.price = BigDecimal.ZERO;
	}

	public Price(final BigDecimal price) {
		this.price = BigDecimal.valueOf(price.longValue());
	}

	public boolean lessThanZero() {
		return price.compareTo(BigDecimal.ZERO) < 0;
	}

	public BigDecimal value() {
		return BigDecimal.valueOf(price.intValue());
	}
}
