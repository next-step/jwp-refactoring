package kitchenpos.dto;

public class MenuProductRequest {
    private Long productId;
    private int quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
