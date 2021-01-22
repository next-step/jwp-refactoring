package kitchenpos.common.entity;

import kitchenpos.common.application.ValidationException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price implements Comparable<Price> {
	public static Price ZERO = new Price(BigDecimal.ZERO);
	static final String MSG_PRICE_NOT_NULL = "price must be not null";
	static final String MSG_PRICE_MUST_EQUAL_OR_GREATER_THAN_ZERO = "price must be equal or greater than zero";

	@Column(name = "price", nullable = false, columnDefinition = "DECIMAL(19,2)")
	private final BigDecimal price;

	protected Price() {
		this(new BigDecimal(0L));
	}

	public Price(BigDecimal price) {
		validateNull(price);
		validateBigDecimal(price);
		this.price = price;
	}

	private void validateNull(Object price) {
		if (price == null) {
			throw new ValidationException(MSG_PRICE_NOT_NULL);
		}
	}

	private void validateBigDecimal(BigDecimal price) {
		if (price.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException(MSG_PRICE_MUST_EQUAL_OR_GREATER_THAN_ZERO);
		}
	}

	@Override
	public int compareTo(Price o) {
		return this.price.compareTo(o.price);
	}

	public Price add(Price price) {
		// TODO: 2021-01-19 add test
		return new Price(this.price.add(price.price));
	}

	public Price multiply(Quantity quantity) {
		// TODO: 2021-01-19 add test
		final BigDecimal multiplicand = new BigDecimal(quantity.getValue());
		return new Price(this.price.multiply(multiplicand));
	}

	public BigDecimal getValue() {
		return price;
	}
}
