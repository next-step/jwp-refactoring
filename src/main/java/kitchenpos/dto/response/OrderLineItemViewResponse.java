package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemViewResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public static OrderLineItemViewResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemViewResponse(
                orderLineItem.getId(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getMenu().getId(),
                orderLineItem.getQuantity().toLong()
        );
    }

    public OrderLineItemViewResponse() {
    }

    public OrderLineItemViewResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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
