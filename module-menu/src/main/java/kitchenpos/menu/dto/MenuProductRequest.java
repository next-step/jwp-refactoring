package kitchenpos.menu.dto;

public class MenuProductRequest {
    private final Long productId;
    private final Integer quantity;

    public MenuProductRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
