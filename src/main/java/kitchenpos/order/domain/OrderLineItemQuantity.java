package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {
    private static final int MIN_VALUE = 1;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (quantity < MIN_VALUE) {
            throw new IllegalArgumentException("주문 항목의 수량은 0 이하 일 수 없습니다.");
        }
    }

    public static OrderLineItemQuantity of(long quantity) {
        return new OrderLineItemQuantity(quantity);
    }

    public long getQuantity() {
        return quantity;
    }
}
