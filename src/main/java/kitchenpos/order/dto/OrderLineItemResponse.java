package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> listOf(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::new)
                .collect(Collectors.toList());
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
