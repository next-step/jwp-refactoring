package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    @Column(name = "quantity", nullable = false)
    private Long value;

    protected Quantity() {
    }

    public Quantity(Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(Long value) {
        if (value < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        }
    }

    public Long getValue() {
        return value;
    }
}
