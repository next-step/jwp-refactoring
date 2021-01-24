package kitchenpos.order.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Quantity {
	public static final int QUANTITY_MINIMUM_COUNT = 0;
	private long quantity;

	protected Quantity() {
	}

	private Quantity(final long quantity) {
		validateQuantity(quantity);
		this.quantity = quantity;
	}

	public static Quantity of(final long quantity) {
		return new Quantity(quantity);
	}

	public long getQuantity() {
		return quantity;
	}

	private void validateQuantity(long quantity) {
		if (quantity <= QUANTITY_MINIMUM_COUNT) {
			throw new IllegalArgumentException("수량은 1개 이상이여야 합니다.");
		}
	}
}


