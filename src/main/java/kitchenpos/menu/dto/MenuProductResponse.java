package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {
    private final Long seq;
    private final ProductResponse product;
    private final Integer quantity;

    public MenuProductResponse(Long seq, ProductResponse product, Integer quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        Product product = menuProduct.getProduct();
        return new MenuProductResponse(
                menuProduct.getSeq(),
                ProductResponse.from(product),
                menuProduct.getQuantity().getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
