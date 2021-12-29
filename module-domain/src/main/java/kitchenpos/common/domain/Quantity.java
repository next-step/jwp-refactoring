package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity extends Value<Quantity> {
	@Column(name = "quantity", nullable = false)
	private long value;

	protected Quantity() {
	}

	public static Quantity from(Long value) {
		if (value == null || value < 0) {
			throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
		}

		Quantity quantity = new Quantity();
		quantity.value = value;
		return quantity;
	}

	public long getValue() {
		return value;
	}
}
