package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
	private BigDecimal price;

	public Price(BigDecimal price) {
		validatePriceExistsAndBiggerThanZero(price);
		this.price = price;
	}

	private void validatePriceExistsAndBiggerThanZero(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
	}
}
