package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    @Column(name = "quantity")
    private Long quantity;

    public Quantity() {
    }

    public Quantity(Long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(final Long quantity) {
        if (null == quantity || quantity < 1) {
            throw new IllegalArgumentException("수량은 1 이상의 숫자여야 합니다.");
        }
    }

    public long value() {
        return quantity;
    }
}
