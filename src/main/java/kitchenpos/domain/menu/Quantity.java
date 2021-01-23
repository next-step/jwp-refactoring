package kitchenpos.domain.menu;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private int quantity;

    protected Quantity() {}

    public Quantity(int quantity) {
        checkGreaterThanZero(quantity);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void checkGreaterThanZero(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("마이너스 수량을 가질 수 없습니다");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity)) return false;
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
