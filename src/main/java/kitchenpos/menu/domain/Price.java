package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.Assert;

@Embeddable
public class Price {

	public static final Price ZERO = Price.from(BigDecimal.ZERO);


	@Column(name = "price")
	private BigDecimal value;

	private Price(BigDecimal value) {
		Assert.notNull(value, "가격은 필수입니다.");
		Assert.isTrue(graterThanOrEqualToZero(value), String.format("가격(%s)은 0 이상이어야 합니다.", value));
		this.value = value;
	}

	protected Price() {
	}

	public static Price from(BigDecimal value) {
		return new Price(value);
	}

	public BigDecimal value() {
		return value;
	}

	private boolean graterThanOrEqualToZero(BigDecimal value) {
		return value.compareTo(BigDecimal.ZERO) >= 0;
	}

	public Price sum(Price price) {
		return Price.from(value.add(price.value));
	}

	public Price multiply(long value) {
		return Price.from(this.value.multiply(BigDecimal.valueOf(value)));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Price price = (Price)o;
		return Objects.equals(value, price.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public boolean graterThanOrEqualTo(Price price) {
		return value.compareTo(price.value) >= 0;
	}
}
