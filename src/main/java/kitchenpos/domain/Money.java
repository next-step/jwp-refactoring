package kitchenpos.domain;

import java.util.Objects;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-13
 */
public class Money {

    public long amount;

    private Money(long amount) {
        if (amount < 0L) {
            throw new IllegalArgumentException("0보다 작은 숫자는 들어올 수 없습니다.");
        }
        this.amount = amount;
    }

    public static Money won(long amount) {
        return new Money(amount);
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
        return amount == money.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
