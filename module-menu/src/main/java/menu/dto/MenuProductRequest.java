package menu.dto;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    protected MenuProductRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }


}
