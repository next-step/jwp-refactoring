package kitchenpos.order.dto;

public class CreateOrderLineItemRequest {
    private Long orderId;
    private Long menuId;
    private long quantity;

    public CreateOrderLineItemRequest() { }

    public CreateOrderLineItemRequest(Long menuId, long quantity) {
        this(null, menuId, quantity);
    }

    public CreateOrderLineItemRequest(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public CreateOrderLineItemDto toDomainDto() {
        return new CreateOrderLineItemDto(orderId, menuId, quantity);
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
