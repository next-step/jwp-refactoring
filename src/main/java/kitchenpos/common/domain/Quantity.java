package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Quantity {
    public static final Long ZERO = 0L;

    @Column(nullable = false)
    private Long quantity;

    protected Quantity() {}

    public Quantity(Long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(Long quantity) {
        if (quantity < ZERO) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다.");
        }
    }

    public Long value() {
        return quantity;
    }
}
