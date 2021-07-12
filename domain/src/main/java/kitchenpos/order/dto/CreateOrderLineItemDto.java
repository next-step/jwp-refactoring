package kitchenpos.order.dto;

public class CreateOrderLineItemDto {
    private Long orderId;
    private Long menuId;
    private long quantity;

    public CreateOrderLineItemDto() { }

    public CreateOrderLineItemDto(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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
