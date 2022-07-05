package kitchenpos.common.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    @Column(name = "quantity")
    private Integer quantity;

    public Quantity() {
    }

    public Quantity(final Integer quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(final Integer quantity) {
        if (Objects.isNull(quantity) || quantity < 1) {
            throw new IllegalArgumentException("수량은 1 이상의 숫자여야 합니다.");
        }
    }

    public int value() {
        return quantity;
    }
}
