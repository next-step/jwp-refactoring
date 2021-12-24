package kitchenpos.menu.dto;

public class MenuProductRequest {

    private long productId;
    private long quantity;

    protected MenuProductRequest() {
    }

    private MenuProductRequest(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
