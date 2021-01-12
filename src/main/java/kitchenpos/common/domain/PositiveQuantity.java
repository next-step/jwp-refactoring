package kitchenpos.common.domain;

import javax.persistence.Embeddable;

import io.micrometer.core.instrument.util.StringUtils;

@Embeddable
public class PositiveQuantity {
	private long quantity;

	protected PositiveQuantity() {
	}

	public PositiveQuantity(long quantity) {
		validate(quantity);
		this.quantity = quantity;
	}

	private void validate(long quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("수량은 0이거나 음수 일 수 없습니다.");
		}
	}

	public long value() {
		return this.quantity;
	}
}
