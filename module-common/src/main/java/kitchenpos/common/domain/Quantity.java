package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private static final int ZERO_COUNT = 0;

    @Column(nullable = false)
    private int quantity;

    protected Quantity() {
    }

    public Quantity(Integer quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(Integer quantity) {
        if (quantity == null || quantity < ZERO_COUNT) {
            throw new IllegalArgumentException("[ERROR] 수량은 1개 이상 이어야 합니다.");
        }
    }

    public int getQuantity() {
        return quantity;
    }
}
