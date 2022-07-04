package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    private static final String ERROR_MESSAGE_EMPTY = "수량이 비어있습니다.";
    private static final String ERROR_MESSAGE_INVALID = "수량은 0 보다 커야 합니다.";

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    public Quantity(Long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(Long quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EMPTY);
        }
        if (quantity < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID);
        }
    }

    public Long getQuantity() {
        return quantity;
    }
}
