package kitchenpos.dto.menu;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    protected MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(final Long productId, final long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
