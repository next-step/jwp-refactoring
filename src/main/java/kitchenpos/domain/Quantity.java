package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    private Long quantity;

    protected Quantity() {
    }

    public Quantity(Long quantity) {
        verifyAvailable(quantity);
        this.quantity = quantity;
    }

    private void verifyAvailable(Long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 음수가 될 수 없습니다.");
        }
    }

    public Long value() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity1 = (Quantity) o;
        return Objects.equals(quantity, quantity1.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
