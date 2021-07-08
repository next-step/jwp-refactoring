package kitchenpos.menu.dto;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest() {
        // empty
    }

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
