package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class Price implements Comparable<Price> {
	private static final int MIN_PRICE = 0;

	@Column
	private BigDecimal amount;

	protected Price() {}

	private Price(BigDecimal amount) {
		validateNonNegative(amount);
		this.amount = amount;
	}

	static Price wonOf(int amount) {
		return new Price(BigDecimal.valueOf(amount));
	}

	static Price wonOf(BigDecimal amount) {
		return new Price(amount);
	}

	BigDecimal getAmount() {
		return amount;
	}

	Price times(long n) {
		BigDecimal otherAmount = BigDecimal.valueOf(n);
		return Price.wonOf(amount.multiply(otherAmount));
	}

	Price plus(Price otherPrice) {
		BigDecimal add = this.amount.add(otherPrice.getAmount());
		return Price.wonOf(add);
	}

	private void validateNonNegative(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) < MIN_PRICE) {
			throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Price price = (Price)o;
		return Objects.equals(getAmount(), price.getAmount());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAmount());
	}

	@Override
	public int compareTo(Price price) {
		return this.amount.compareTo(price.amount);
	}
}
