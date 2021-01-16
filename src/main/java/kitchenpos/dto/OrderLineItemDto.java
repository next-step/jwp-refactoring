package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

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

    public OrderLineItemDto() {
    }

    public OrderLineItemDto(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(OrderLineItem item, Long orderId) {
        return new OrderLineItemDto(item.getId(), orderId, item.getMenuId(), item.getQuantity());
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
