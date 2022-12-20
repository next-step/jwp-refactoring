package kitchenpos.common.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.menu.domain.Price;

@Embeddable
public class Quantity {

	@Column(name = "quantity", nullable = false, updatable = false)
	private long value;

	protected Quantity() {
	}

	private Quantity(long value) {
		this.value = value;
	}

	public static Quantity from(long value) {
		return new Quantity(value);
	}

	public long value() {
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
		Quantity quantity1 = (Quantity)o;
		return value == quantity1.value;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	public Price multiply(Price price) {
		return price.multiply(value);
	}
}
