package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {

    private Long id;
    private ProductResponse product;
    private long quantity;

    private MenuProductResponse() {
    }

    private MenuProductResponse(Long seq, ProductResponse product, long quantity) {
        this.id = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(),
            ProductResponse.from(menuProduct.getProduct()),
            menuProduct.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
