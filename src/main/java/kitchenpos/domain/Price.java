package kitchenpos.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.ErrorCode;
import kitchenpos.product.exception.ProductException;

@Embeddable
public class Price {

	private static final int ZERO = 0;

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
			throw new ProductException(ErrorCode.PRICE_IS_NOT_NULL);
		}
	}

	private static void validateNegativePrice(int intValue) {
		if (intValue < ZERO) {
			throw new ProductException(ErrorCode.PRICE_NOT_NEGATIVE_NUMBER);
		}
	}

	public BigDecimal getPrice() {
		return price;
	}
}
