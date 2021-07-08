package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private long seq;
    private long menuId;
    private long quantity;

    public OrderLineItemResponse() {}

    private OrderLineItemResponse(long seq, long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        final long menuId = orderLineItem.menuId();
        return new OrderLineItemResponse(orderLineItem.seq(), menuId, orderLineItem.quantityToLong());
    }

    public static List<OrderLineItemResponse> listOf(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    public long getSeq() {
        return seq;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
