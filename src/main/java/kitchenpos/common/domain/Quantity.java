package kitchenpos.common.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    static final Long MINIMUM_QUANTITY = 1L;

    @Column(nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    private Quantity(final Long quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("수량은 null 일 수 없습니다.");
        }

        if (quantity < MINIMUM_QUANTITY) {
            throw new IllegalArgumentException("수량은 {}개 이상이어야 합니다.");
        }

        this.quantity = quantity;
    }

    public static Quantity of(final Long quantity) {
        return new Quantity(quantity);
    }

    public Long getQuantity() {
        return quantity;
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(quantity);
    }
}
