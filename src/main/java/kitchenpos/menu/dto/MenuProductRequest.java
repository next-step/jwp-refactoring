package kitchenpos.menu.dto;

import kitchenpos.product.domain.Product;

public class MenuProductRequest {

    private Long productId;
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean match(Product product) {
        return product.matchId(productId);
    }
}
