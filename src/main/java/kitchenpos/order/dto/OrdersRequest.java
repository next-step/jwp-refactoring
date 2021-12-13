package kitchenpos.order.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.utils.StreamUtils;

public class OrdersRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

    protected OrdersRequest() {}

    public OrdersRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Orders toOrders() {
        Orders orders = Orders.of(OrderTable.from(orderTableId), Optional.ofNullable(orderStatus)
                                                                         .orElse(OrderStatus.COOKING));
        orders.setOrderLineItems(StreamUtils.mapToList(orderLineItems,
                                                       request -> OrderLineItem.of(orders,
                                                                                   Menu.from(request.getMenuId()),
                                                                                   request.getQuantity())));

        return orders;
    }
}
