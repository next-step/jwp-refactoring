package kitchenpos.menu.dto;

public class MenuProductRequest {
    private Long menuId;
    private Long productId;
    private Long quantity;

    protected MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return this.productId;
    }

    public Long getQuantity() {
        return this.quantity;
    }
}
