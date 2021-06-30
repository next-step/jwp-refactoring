package kitchenpos.dto.response;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderViewResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemViewResponse> orderLineItems;


    public static OrderViewResponse of(Order order) {
        List<OrderLineItemViewResponse> itemViewResponses = order.getOrderLineItems()
                .stream()
                .map(OrderLineItemViewResponse::of)
                .collect(Collectors.toList());

        return new OrderViewResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                itemViewResponses
        );
    }

    public OrderViewResponse() {
    }

    public OrderViewResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemViewResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemViewResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
