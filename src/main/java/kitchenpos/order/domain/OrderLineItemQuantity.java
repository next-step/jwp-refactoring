package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {

    private static final int NO_ORDER_COUNT = 0;

    @Column(nullable = false)
    private int quantity;

    protected OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(Integer quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(Integer quantity) {
        if(quantity == null || quantity <= NO_ORDER_COUNT){
            throw new IllegalArgumentException("[ERROR] 주문 수량은 1개이상 이어야 합니다.");
        }
    }

    public int getQuantity() {
        return quantity;
    }
}
