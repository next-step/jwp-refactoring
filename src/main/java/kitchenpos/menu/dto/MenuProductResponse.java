package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {

    private final Long productId;
    private final Long quantity;

    public MenuProductResponse(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> list(MenuProducts menuProducts) {
        return menuProducts.getAll().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    private static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProductId(), menuProduct.getQuantity().value());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
