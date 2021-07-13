package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Amount extends Number {
    private BigDecimal amount;

    public Amount() { }

    public Amount(BigDecimal amount) {
        this.amount = amount;
    }

    public Amount(int amount) {
        this.amount = BigDecimal.valueOf(amount);
    }

    public void checkIfOverThan(BigDecimal sum) {
        if (amount.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal get() {
        return amount;
    }

    @Override
    public int intValue() {
        return amount.intValue();
    }

    @Override
    public long longValue() {
        return amount.longValue();
    }

    @Override
    public float floatValue() {
        return amount.floatValue();
    }

    @Override
    public double doubleValue() {
        return amount.doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount1 = (Amount) o;
        return Objects.equals(amount, amount1.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

}
