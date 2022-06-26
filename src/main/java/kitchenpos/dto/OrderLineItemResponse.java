package kitchenpos.dto;

public class OrderLineItemResponse {
    private Long id;
    private Long menuId;
    private int quantity;

    protected OrderLineItemResponse() {
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
