package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public static OrderLineItemResponse of(final Long orderId, final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderId, orderLineItem);
    }

    public static List<OrderLineItemResponse> toList(Long orderId,
        List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(orderLineItem -> OrderLineItemResponse.of(orderId, orderLineItem))
            .collect(Collectors.toList());
    }

    public OrderLineItemResponse() {
    }

    private OrderLineItemResponse(final Long orderId, final OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.orderId = orderId;
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
