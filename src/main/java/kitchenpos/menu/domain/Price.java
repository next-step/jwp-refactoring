package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.menu.exception.PriceException;

@Embeddable
public class Price {

	private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

	@Column(nullable = false)
	private BigDecimal price;

	protected Price() {

	}

	public Price(BigDecimal price) {
		validate(price);
		this.price = price;
	}

	public BigDecimal multiply(BigDecimal otherValue) {
		return this.price.multiply(otherValue);
	}

	public BigDecimal add(BigDecimal otherValue) {
		return this.price.add(otherValue);
	}

	public BigDecimal value() {
		return price;
	}

	private void validate(BigDecimal price) {
		nonNullValidate(price);
		moreThanZeroValidate(price);
	}

	private void nonNullValidate(BigDecimal price) {
		if (Objects.isNull(price)) {
			throw new PriceException("가격은 비워져있을 수 없습니다.");
		}
	}

	private void moreThanZeroValidate(BigDecimal price) {
		if (price.compareTo(MIN_PRICE) < 0) {
			throw new PriceException("가격은 0보다 작을 수 없습니다.");
		}
	}

	public int compareTo(Price price) {
		return this.price.compareTo(price.price);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Price price1 = (Price)o;
		return Objects.equals(price, price1.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(price);
	}
}
