package kitchenpos.dto;

public class MenuProductResponse {
    private Long id;
    private Long productId;
    private int quantity;

    protected MenuProductResponse() {
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
