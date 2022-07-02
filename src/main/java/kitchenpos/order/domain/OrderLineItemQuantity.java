package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {
    @Column(name = "quantity", nullable = false)
    private Long value;

    public OrderLineItemQuantity() {
        this.value = 0L;
    }

    public OrderLineItemQuantity(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("수량은 0개 이상으로 입력해주세요.");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
