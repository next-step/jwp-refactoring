package kitchenpos.menu.dto;

public class MenuProductRequest {

    private long productId;
    private long quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

}
