package kitchenpos.dto;

public class ProductIdQuantityPair {
    private Long productId;
    private Long quantity;

    public ProductIdQuantityPair() {
    }

    public ProductIdQuantityPair(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
