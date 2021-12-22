package kitchenpos.common.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.PriceException;

@Embeddable
public class Price {
	public static final int ZERO = 0;
	public static Price ZERO_PRICE = Price.from(ZERO);

	@Column(nullable = false)
	private BigDecimal price;

	protected Price() {
	}

	private Price(BigDecimal price) {
		this.price = price;
	}

	public static Price from(Integer intValue) {
		validatePrice(intValue);
		return new Price(new BigDecimal(intValue));
	}

	private static void validatePrice(Integer intValue) {
		validateIsNullPrice(intValue);
		validateNegativePrice(intValue);
	}

	private static void validateIsNullPrice(Integer intValue) {
		if (intValue == null) {
			throw new PriceException(ErrorCode.PRICE_IS_NOT_NULL);
		}
	}

	private static void validateNegativePrice(Integer intValue) {
		if (intValue < ZERO) {
			throw new PriceException(ErrorCode.PRICE_NOT_NEGATIVE_NUMBER);
		}
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Price plus(Price addPrice) {
		return Price.from(price.add(addPrice.getPrice()).intValue());
	}

	public boolean compare(Price comparePrice) {
		return price.compareTo(comparePrice.getPrice()) > ZERO;
	}
}
