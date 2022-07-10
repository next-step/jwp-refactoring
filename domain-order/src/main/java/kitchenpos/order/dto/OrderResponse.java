package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long orderId;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<Long> menuIds;

    public OrderResponse() {
    }

    public OrderResponse(final Order order) {
        this.orderId = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        if (Objects.nonNull(order.getOrderLineItems())) {
            this.menuIds = order.getOrderLineItems().stream()
                    .map(orderLineItem -> orderLineItem.getMenu().getId())
                    .collect(Collectors.toList());
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}
