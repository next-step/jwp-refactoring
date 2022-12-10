package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderLineItemRequest {
    private Long menu;
    private Long quantity;

    protected OrderLineItemRequest() {}

    public OrderLineItemRequest(Long menu, Long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OrderLineItem createOrderLineItem() {
        return new OrderLineItem(menu, quantity);
    }
}
