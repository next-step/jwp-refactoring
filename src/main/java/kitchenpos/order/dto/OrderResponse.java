package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponseList;

    public OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItemResponseList) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemResponseList = orderLineItemResponseList;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(), order.getOrderTable().getId(), order.getOrderStatus().name(),
                order.getOrderedTime(), createOrderLineItemResponses(order.getOrderLineItems())
        );
    }

    private static List<OrderLineItemResponse> createOrderLineItemResponses(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItemResponse.of(orderLineItem))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
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

    public List<OrderLineItemResponse> getOrderLineItemResponseList() {
        return orderLineItemResponseList;
    }

    public List<Long> createOrderLineItemResponseSeqs() {
        return orderLineItemResponseList.stream()
                .map(orderLineItemResponse ->  orderLineItemResponse.getSeq())
                .collect(Collectors.toList());
    }
}
