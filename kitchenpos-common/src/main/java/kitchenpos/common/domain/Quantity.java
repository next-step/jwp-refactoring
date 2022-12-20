package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    @Column(nullable = false)
    private long quantity;

    protected Quantity() {}

    public Quantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (quantity < 0L) {
            throw new IllegalArgumentException(String.format("수량은 0보다 작을 수 없습니다. input = %d", quantity));
        }
    }

    public long value() {
        return quantity;
    }
}
