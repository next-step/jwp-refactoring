package kitchenpos.dto.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table.OrderTable;
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
