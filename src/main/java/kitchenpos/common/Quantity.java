package kitchenpos.common;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

	public static final int MIN_INCLUSIVE = 0;

	@Column(name = "quantity", nullable = false)
	private long quantity;

	protected Quantity() {
	}

	private Quantity(long quantity) {
		validate(quantity);
		this.quantity = quantity;
	}

	private void validate(long quantity) {
		if (quantity < MIN_INCLUSIVE) {
			throw new InvalidQuantityException();
		}
	}

	public static Quantity of(long quantity) {
		return new Quantity(quantity);
	}

	public long getQuantity() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Quantity)) {
			return false;
		}
		Quantity quantity1 = (Quantity)o;
		return quantity == quantity1.quantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(quantity);
	}
}
