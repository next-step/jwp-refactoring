package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private long quantity;

    protected Quantity() {

    }

    public Quantity(long quantity) {
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity quantity1 = (Quantity) o;
        return getQuantity() == quantity1.getQuantity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantity());
    }
}
