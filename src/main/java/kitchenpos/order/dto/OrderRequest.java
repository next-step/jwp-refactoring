package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItems;

    public static OrderRequest from(Order order) {
        List<OrderLineItemRequest> orderLineItemRequests = order.getOrderLineItems().stream()
                .map(OrderLineItemRequest::new)
                .collect(Collectors.toList());
        return new OrderRequest(order.getOrderTableId(), order.getOrderStatusName(), orderLineItemRequests);
    }

    public OrderRequest(Long orderTableId, String orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder() {
        Order order = createOrder();
        order.updateOrderStatus(OrderStatus.valueOf(orderStatus));
        return order;
    }

    public Order createOrder() {
        /*Order order = new Order(orderTableId);
        orderLineItems.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .forEach(order::addOrderLineItem);
        return order;*/
        List<OrderLineItem> orderLineItems1 = orderLineItems.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
        return Order.createOrder(orderTableId, orderLineItems1);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
