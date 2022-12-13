package kitchenpos.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.InvalidMoneyValueException;

@Embeddable
public class Money {

	private static final int SCALE = 0;
	private static final RoundingMode ROUNDING_MODE = RoundingMode.FLOOR;

	private BigDecimal value;

	protected Money() {
	}

	private Money(BigDecimal value) {
		this.value = value;
	}

	public static Money valueOf(BigDecimal value) {
		validate(value);
		return new Money(value.setScale(SCALE, ROUNDING_MODE));
	}

	public static Money valueOf(int value) {
		return valueOf(BigDecimal.valueOf(value));
	}

	private static void validate(BigDecimal value) {
		if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidMoneyValueException();
		}
	}

	public boolean isEqualTo(int other) {
		Money otherMoney = valueOf(other);
		return value.compareTo(otherMoney.value) == 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Money money = (Money) o;
		return value.equals(money.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	public BigInteger toBigInteger() {
		return value.toBigInteger();
	}
}
