package kitchenpos.common.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

	private final Long quantity;

	private Quantity(final Long quantity) {
		this.quantity = quantity;
	}

	protected Quantity() {
		this.quantity = 0L;
	}

	public static Quantity valueOf(final Long quantity) {
		return new Quantity(quantity);
	}

	public long value() {
		return quantity;
	}
}
