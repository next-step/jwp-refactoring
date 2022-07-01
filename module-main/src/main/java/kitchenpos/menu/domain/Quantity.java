package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    @Column(name = "quantity")
    private long quantity;

    protected Quantity() {
    }

    private Quantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    public static Quantity from(long quantity) {
        return new Quantity(quantity);
    }

    public long getValue() {
        return quantity;
    }

    private void validate(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량이 0보다 작을 수 없습니다.");
        }
    }
}
