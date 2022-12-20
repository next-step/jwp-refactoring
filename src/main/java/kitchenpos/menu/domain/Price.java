package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Price {
	private final BigDecimal value;

	private Price(BigDecimal value) {
		this.value = value;
	}

	public static Price from(BigDecimal value) {
		return new Price(value);
	}

	public BigDecimal getValue() {
		return value;
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
}
