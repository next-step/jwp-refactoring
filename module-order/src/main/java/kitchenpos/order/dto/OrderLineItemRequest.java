package kitchenpos.order.dto;

public class OrderLineItemRequest {
    private Long menu;
    private Long quantity;

    protected OrderLineItemRequest() {}

    public OrderLineItemRequest(Long menu, Long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
