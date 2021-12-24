package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceFixture {
    private OrderServiceFixture() {
    }

    public static List<OrderLineItem> getOrderLineItems(OrderLineItem... orderLineItems) {
        return Arrays.stream(orderLineItems)
                .collect(Collectors.toList());
    }

    public static List<OrderLineItemRequest> getOrderLineRequests(OrderLineItem... orderLineItems) {
        return Arrays.stream(orderLineItems)
                .map(OrderServiceFixture::getOrderLineRequest)
                .collect(Collectors.toList());
    }

    public static OrderLineItemRequest getOrderLineRequest(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public static OrderLineItem getOrderLineItem(long menuId, int quantity) {
        return OrderLineItem.of(menuId, quantity);
    }

    public static Order getOrder(long id, long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.generate(id, orderTableId, orderStatus, orderLineItems);
    }

    public static Order getOrder(long id, long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.generate(id, orderTableId, orderLineItems);
    }

    public static OrderRequest getChangeRequest(String status) {
        return new OrderRequest(status);
    }

    public static OrderRequest getCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }
}
