package kitchenpos.order.ui.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

public final class OrderLineItemResponse {

    private long seq;
    private long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(long seq, long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    static List<OrderLineItemResponse> listFrom(OrderLineItems orderLineItems) {
        return orderLineItems.list()
            .stream()
            .map(OrderLineItemResponse::from)
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

    private static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getSeq(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity().value()
        );
    }
}
