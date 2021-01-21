package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {

	static final String MSG_QUANTITY_MUST_EQUAL_OR_GREATER_THAN_ZERO = "quantity must be equal or greater than zero";

	@Column(name = "quantity", nullable = false)
	private final long quantity;

	protected Quantity() {
		this(0);
	}

	public Quantity(long quantity) {
		validate(quantity);
		this.quantity = quantity;
	}

	private void validate(long quantity) {
		if (quantity < 0) {
			throw new ValidationException(MSG_QUANTITY_MUST_EQUAL_OR_GREATER_THAN_ZERO);
		}
	}

	public long getValue() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Quantity)) return false;
		Quantity quantity1 = (Quantity) o;
		return quantity == quantity1.quantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(quantity);
	}
}
