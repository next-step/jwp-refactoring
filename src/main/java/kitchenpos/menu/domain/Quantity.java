package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.menu.exception.QuantityException;

@Embeddable
public class Quantity {

	private static final long MIN_QUANTITY = 1;

	@Column(nullable = false)
	private long quantity;

	public Quantity(long quantity) {
		validate(quantity);
		this.quantity = quantity;
	}

	public long value() {
		return quantity;
	}

	private void validate(long quantity) {
		if (quantity < MIN_QUANTITY) {
			throw new QuantityException("수량은 1개 미만의 숫자가 될 수 없습니다.");
		}
	}
}
