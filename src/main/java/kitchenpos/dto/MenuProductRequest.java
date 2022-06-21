package kitchenpos.dto;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest() {
    }

    public Long getProductId() {
        return this.productId;
    }

    public long getQuantity() {
        return this.quantity;
    }
}
