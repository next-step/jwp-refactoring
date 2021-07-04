package kitchenpos.dto.request;

import kitchenpos.domain.order.OrderCreate;
import kitchenpos.domain.order.OrderLineItemCreate;
import kitchenpos.domain.order.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public OrderCreate toCreate() {
        List<OrderLineItemCreate> orderLineItemCreates = orderLineItems.stream()
                .map(OrderLineItemCreateRequest::toCreate)
                .collect(Collectors.toList());

        return new OrderCreate(
                orderTableId,
                orderStatus,
                orderLineItemCreates
        );
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
