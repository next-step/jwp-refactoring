package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price implements Comparable<Price> {
	public static Price ZERO = new Price(BigDecimal.ZERO);

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
			throw new IllegalArgumentException("");
		}
	}

	private void validateBigDecimal(BigDecimal price) {
		if (price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("");
		}
	}

	@Override
	public int compareTo(Price o) {
		return this.price.compareTo(o.price);
	}

	public Price add(Price price) {
		return new Price(this.price.add(price.price));
	}

	public Price multiply(BigDecimal multiplier) {
		return new Price(this.price.multiply(multiplier));
	}

	public BigDecimal getValue() {
		return price;
	}
}
