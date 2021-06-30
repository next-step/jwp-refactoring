package kitchenpos.domain;

import java.math.BigDecimal;


public class Price {

	private final BigDecimal amount;

	private Price(BigDecimal amount) {
		validateNonNegative(amount);
		this.amount = amount;
	}

	public static Price wonOf(int amount) {
		return new Price(BigDecimal.valueOf(amount));
	}

	public static Price wonOf(BigDecimal amount) {
		return new Price(amount);
	}

	private void validateNonNegative(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
		}
	}

	BigDecimal getAmount() {
		return amount;
	}
}
