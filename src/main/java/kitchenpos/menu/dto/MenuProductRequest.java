package kitchenpos.menu.dto;

import java.util.Objects;

public class MenuProductRequest {

    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public boolean isSameProductId(Long productId) {
        return Objects.equals(this.productId, productId);
    }
}
