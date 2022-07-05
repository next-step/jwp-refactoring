package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    @Column(name = "quantity", nullable = false)
    private long value;

    protected Quantity() {
        this(0);
    }

    protected Quantity(long quantity) {
        validateQuantity(quantity);
        this.value = quantity;
    }

    public static Quantity from(long quantity) {
        return new Quantity(quantity);
    }

    private void validateQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량이 0보다 작을 수 없습니다.");
        }
    }

    public Long value() {
        return value;
    }

}
