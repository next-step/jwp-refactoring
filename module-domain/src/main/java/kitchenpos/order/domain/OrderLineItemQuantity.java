package kitchenpos.order.domain;

import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {
    private Long quantity;

    protected OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long toLong() {
        return quantity;
    }
}
