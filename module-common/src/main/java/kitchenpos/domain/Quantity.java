package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private static final Long MIN_VALUE = 0L;

    @Column(nullable = false)
    private Long quantity;

    protected Quantity() {}

    public Quantity(Long value) {
        checkValue(value);

        this.quantity = value;
    }

    public Long getValue() {
        return quantity;
    }

    private void checkValue(Long value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다.");
        }
    }
}
