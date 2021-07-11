package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {
    private Long id;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemDto() {}

    public OrderLineItemDto(final Long orderId, final Long menuId, final long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItemDto(final Long id, final Long orderId, final Long menuId, final long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto from(final OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getId(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getMenu().getId(),
                orderLineItem.getQuantity());
    }

    public Long getId() {
        return id;
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
