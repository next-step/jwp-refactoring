package kitchenpos.order.dto;

public class OrderLineItemRequest {

    private Long orderId;
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemRequest(Long orderId, Long menuId, Long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getQuantity() {
        return quantity;
    }

}
