package kitchenpos.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Embeddable;

@Embeddable
public class Money {

	private static final int SCALE = 0;
	private static final RoundingMode ROUNDING_MODE = RoundingMode.FLOOR;

	private BigDecimal value;

	protected Money() {
	}

	private Money(BigDecimal value) {
		this.value = value;
	}

	public static Money valueOf(BigDecimal price) {
		return new Money(price.setScale(SCALE, ROUNDING_MODE));
	}

	public BigDecimal toBigDecimal() {
		return value;
	}
}
