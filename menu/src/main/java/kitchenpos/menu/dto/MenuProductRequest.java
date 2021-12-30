package kitchenpos.menu.dto;

import kitchenpos.product.domain.Product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MenuProductRequest {

    @Positive
    @NotNull
    private Long productId;

    @Positive
    @NotNull
    private Long quantity;

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public boolean match(Product product) {
        return product.matchId(productId);
    }
}
