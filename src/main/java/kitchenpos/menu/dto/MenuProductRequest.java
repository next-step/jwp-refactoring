package kitchenpos.menu.dto;

import javax.validation.constraints.Min;

public class MenuProductRequest {
    private long productId;
    @Min(1)
    private long quantity;

    protected MenuProductRequest(){}

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
