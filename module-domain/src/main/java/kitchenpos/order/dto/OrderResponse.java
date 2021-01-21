package kitchenpos.order.dto;

import kitchenpos.order.Order;
import kitchenpos.order.OrderStatus;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderResponse {
    private static final String ERR_TEXT_INVALID_ORDER = "유효하지 않은 주문 상품 데이터입니다.";

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    protected OrderResponse(final Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        this.orderLineItems = order.getOrderLineItems().stream()
            .map(OrderLineItemResponse::of)
            .collect(toList());
    }

    public static OrderResponse of(final Order order) {
        if (order == null) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_ORDER);
        }

        return new OrderResponse(order);
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
