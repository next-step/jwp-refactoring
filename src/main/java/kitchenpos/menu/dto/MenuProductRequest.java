package kitchenpos.menu.dto;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest() {
    }

    private MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
