package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {

    @Column(precision = 20, nullable = false)
    private long quantity;

    protected OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("주문 항목의 수량은 최소 0개 이상만 입력 가능합니다[quantity:" + quantity + "]");
        }
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}