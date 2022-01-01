package kitchenpos.order.domain;

import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.*;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {}

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        checkOrderLineItemIsEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    private void checkOrderLineItemIsEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 라인은 최소 1개 이상 필요합니다.");
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public List<OrderLineItem> asList() {
        return orderLineItems;
    }
}
