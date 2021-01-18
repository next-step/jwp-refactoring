package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

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
			throw new IllegalArgumentException();
		}
	}

	public long getValue() {
		return quantity;
	}
}
