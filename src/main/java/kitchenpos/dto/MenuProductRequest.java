package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.util.List;

import static kitchenpos.constants.ErrorCodeType.NOT_FOUND_PRODUCT;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

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
}


