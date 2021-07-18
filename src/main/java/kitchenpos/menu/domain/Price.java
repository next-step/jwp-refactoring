package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
	@Column(nullable = false)
	private BigDecimal price;

	public Price() {
	}

	public Price(BigDecimal price) {
		validatePriceExistsAndBiggerThanZero(price);
		this.price = price;
	}

	private void validatePriceExistsAndBiggerThanZero(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getTotalPrice(long quantity) {
		return price.multiply(BigDecimal.valueOf(quantity));
	}

	public boolean isBiggerThan(BigDecimal target) {
		return price.compareTo(target) > 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Price price1 = (Price)o;
		return price.equals(price1.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(price);
	}
}
