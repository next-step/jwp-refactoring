package kitchenpos.order.dto.response;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long id;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long id, Long menuId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
