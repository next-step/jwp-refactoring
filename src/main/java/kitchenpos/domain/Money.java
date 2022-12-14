package kitchenpos.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.InvalidMoneyValueException;

@Embeddable
public class Money {

	public static final Money ZERO = Money.wons(0);
	private static final int SCALE = 0;

	private BigDecimal value;

	protected Money() {
	}

	private Money(BigDecimal value) {
		this.value = value;
	}

	public static Money wons(BigDecimal value) {
		validate(value);
		return new Money(value.setScale(SCALE, RoundingMode.FLOOR));
	}

	public static Money wons(int value) {
		return wons(BigDecimal.valueOf(value));
	}

	private static void validate(BigDecimal value) {
		if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidMoneyValueException();
		}
	}

	public boolean isEqualTo(int other) {
		Money otherMoney = wons(other);
		return value.compareTo(otherMoney.value) == 0;
	}

	public BigInteger toBigInteger() {
		return value.toBigInteger();
	}

	public Money add(Money money) {
		return add(money.value);
	}

	private Money add(BigDecimal other) {
		return Money.wons(value.add(other));
	}

	public BigDecimal multiply(BigDecimal other) {
		return value.multiply(other);
	}

	public BigDecimal toBigDecimal() {
		return value;
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
}
