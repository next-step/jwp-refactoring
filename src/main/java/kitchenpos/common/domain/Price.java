package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Price {
	private BigDecimal price;

	protected Price() {
	}

	public Price(BigDecimal price) {
		validate(price);
		this.price = price;
	}

	public BigDecimal value() {
		return this.price;
	}

	private void validate(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
	}
}
