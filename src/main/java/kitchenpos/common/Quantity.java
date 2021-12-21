package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity extends Value<Quantity> {
	@Column(name = "quantity", nullable = false)
	private long value;

	protected Quantity() {
	}

	public static Quantity of(Long quantity) {
		if (quantity == null || quantity < 0) {
			throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
		}

		Quantity menuProductQuantity = new Quantity();
		menuProductQuantity.value = quantity;
		return menuProductQuantity;
	}

	public long getValue() {
		return value;
	}
}
