package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

	private static final BigDecimal MIN_INCLUSIVE = BigDecimal.ZERO;
	public static final Price MIN_PRICE = Price.of(MIN_INCLUSIVE);

	@Column(name = "price", nullable = false)
	private BigDecimal price;

	protected Price() {
	}

	private Price(BigDecimal price) {
		validate(price);
		this.price = price;
	}

	private void validate(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(MIN_INCLUSIVE) < 0) {
			throw new InvalidPriceException();
		}
	}

	public static Price of(long price) {
		return new Price(BigDecimal.valueOf(price));
	}

	public static Price of(BigDecimal price) {
		return new Price(price);
	}

	public Price add(Price price) {
		return Price.of(this.price.add(price.price));
	}

	public Price multiply(long operand) {
		return Price.of(price.multiply(BigDecimal.valueOf(operand)));
	}

	public boolean isBiggerThan(Price price) {
		return this.price.compareTo(price.price) > 0;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Price)) {
			return false;
		}
		Price price1 = (Price)o;
		return Objects.equals(price, price1.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(price);
	}
}
