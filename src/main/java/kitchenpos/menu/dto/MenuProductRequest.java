package kitchenpos.menu.dto;

import kitchenpos.product.domain.Product;

import javax.validation.constraints.NotNull;

public class MenuProductRequest {

    @NotNull
    private Long productId;

    @NotNull
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
