package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {

    @Column
    private long quantity;

    protected Quantity() {
    }

    private Quantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity는 0이상이어야 합니다.");
        }
        this.quantity = quantity;
    }

    public static Quantity of(long quantity) {
        return new Quantity(quantity);
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
