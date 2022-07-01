package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemDto() {
    }

    public OrderLineItemDto(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, quantity);
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
