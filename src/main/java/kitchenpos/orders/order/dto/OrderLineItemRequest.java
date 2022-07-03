package kitchenpos.orders.order.dto;

import kitchenpos.orders.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private String menuName;
    private Long menuPrice;
    private Long quantity;

    protected OrderLineItemRequest() {
    }

    protected OrderLineItemRequest(Long menuId, String menuName, Long menuPrice, Long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    protected OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItemRequest of(Long menuId, String menuName, Long menuPrice, Long quantity) {
        return new OrderLineItemRequest(menuId, menuName, menuPrice, quantity);
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, menuName, menuPrice, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public Long getMenuPrice() {
        return menuPrice;
    }
}
