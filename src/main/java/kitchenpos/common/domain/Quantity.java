package kitchenpos.common.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

	private static final int MIN_QUANTITY = 0;

	@Column
	private long value;

	protected Quantity() { }

	private Quantity(long value) {
		validateNonNegative(value);
		this.value = value;
	}

	private void validateNonNegative(long value) {
		if (value < MIN_QUANTITY) {
			throw new IllegalArgumentException("수량은 음수가 될 수 없습니다.");
		}
	}

	public static Quantity of(long quantity) {
		return new Quantity(quantity);
	}

	public long getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Quantity quantity = (Quantity)o;
		return getValue() == quantity.getValue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getValue());
	}
}
