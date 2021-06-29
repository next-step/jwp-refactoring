package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemDto() { }

    public OrderLineItemDto(Long menuId, long quantity) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItemDto(Long orderId, Long menuId, long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItemDto(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(OrderLineItem item) {
        return new OrderLineItemDto(item.getSeq(), item.getOrder().getId(), item.getMenu().getId(), item.getQuantity());
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
