package kitchenpos.common.domain;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

import io.micrometer.core.instrument.util.StringUtils;

@Embeddable
public class PositiveQuantity {
	private long quantity;
	private static final int ZERO = 0;

	protected PositiveQuantity() {
	}

	public PositiveQuantity(long quantity) {
		validate(quantity);
		this.quantity = quantity;
	}

	private void validate(long quantity) {
		if (quantity <= ZERO) {
			throw new IllegalArgumentException("수량은 0이거나 음수 일 수 없습니다.");
		}
	}

	public long value() {
		return this.quantity;
	}

	public BigDecimal toBigDecimal() {
		return BigDecimal.valueOf(quantity);
	}
}
