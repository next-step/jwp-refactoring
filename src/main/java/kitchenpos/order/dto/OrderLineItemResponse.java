package kitchenpos.order.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    protected OrderLineItemResponse() {}

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.orderId = orderLineItem.getOrder().getId();
        this.menuId = orderLineItem.getMenu().getId();
        this.quantity = orderLineItem.getQuantity();
    }

    public static List<OrderLineItemResponse> list(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::new)
                .collect(toList());
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

    public Long getQuantity() {
        return quantity;
    }
}
