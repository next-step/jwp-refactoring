package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Price {
	private BigDecimal price = BigDecimal.ZERO;

	public Price() {
	}

	public Price(BigDecimal price) {
		validate(price);
		this.price = price;
	}

	public BigDecimal value() {
		return this.price;
	}

	public boolean isExpensiveThan(Price price) {
		return this.price.compareTo(price.value()) > 0;
	}

	private void validate(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("가격은 NULL 또는 음수 일 수 없습니다.");
		}
	}

	public void add(Price price) {
		this.price = this.price.add(price.value());
	}

	public Price multiply(BigDecimal quantity) {
		return new Price(price.multiply(quantity));
	}
}
