package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private String menuName;
    private long menuPrice;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, String menuName, long menuPrice, long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public long getMenuPrice() {
        return menuPrice;
    }

    public OrderLineItem toEntity() {
        return OrderLineItem.createOrderLineItem(menuId, menuName, menuPrice, quantity);
    }
}
