package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private Integer quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, Integer quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public OrderLineItem toEntity() {
        return OrderLineItem.of(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}