package kitchenpos.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.order.Order;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {
        // empty
    }

    private OrderResponse(final Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus().name();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = order.getOrderLineItems()
                                   .convertAll(orderLineItem -> OrderLineItemResponse.of(order.getId(),
                                                                                         orderLineItem));
    }

    public static OrderResponse of(final Order order) {
        return new OrderResponse(order);
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
