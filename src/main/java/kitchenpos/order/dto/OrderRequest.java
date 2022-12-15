package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {}

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order createOrder(OrderTable orderTable, List<Menu> menus) {
        List<OrderLineItem> orderLineItems = getOrderLineItems().stream()
                .map(item -> item.createOrderLineItem(menus))
                .collect(Collectors.toList());
        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }
}
