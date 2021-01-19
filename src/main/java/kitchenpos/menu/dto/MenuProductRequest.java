package kitchenpos.menu.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public class MenuProductRequest {

    private long productId;

    private long quantity;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public MenuProductRequest() {
    }

    public MenuProductRequest(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
