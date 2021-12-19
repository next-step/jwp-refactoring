package kitchenpos.common.domain;

import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    @Column
    private Long quantity;

    protected Quantity() {
    }

    private Quantity(Long quantity) {
        Assert.notNull(quantity, "수량은 반드시 존재해야 합니다.");
        Assert.isTrue(isMoreThanZero(quantity), "수량은 1개 이상이여야 합니다.");

        this.quantity = quantity;
    }

    private boolean isMoreThanZero(Long quantity) {
        return quantity > 0;
    }

    public static Quantity of(Long quantity) {
        return new Quantity(quantity);
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity)) return false;
        Quantity quantity1 = (Quantity) o;
        return Objects.equals(quantity, quantity1.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    @Override
    public String toString() {
        return "Quantity{" +
                "quantity=" + quantity +
                '}';
    }
}
