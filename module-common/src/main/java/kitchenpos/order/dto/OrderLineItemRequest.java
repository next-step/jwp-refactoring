package kitchenpos.order.dto;

public class OrderLineItemRequest {
    private Long menuId;
    private int quantity;

    public OrderLineItemRequest() {
    }

    private OrderLineItemRequest(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long id, int quantity) {
        return new OrderLineItemRequest(id, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
