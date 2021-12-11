package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false, updatable = false)
    private long value;

    protected Quantity() {
    }

    private Quantity(long value) {
        Assert.isTrue(isZeroOrPositive(value), String.format("수량(%d)은 반드시 0이상 이어야 합니다.", value));
        this.value = value;
    }

    public static Quantity from(long value) {
        return new Quantity(value);
    }

    public long value() {
        return value;
    }

    private boolean isZeroOrPositive(long value) {
        return value >= 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
