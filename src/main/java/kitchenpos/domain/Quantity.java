package kitchenpos.domain;

import javax.persistence.Embeddable;

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
}
