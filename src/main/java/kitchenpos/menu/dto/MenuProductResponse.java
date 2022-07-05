package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private long quantity;
    private long productId;

    public static List<MenuProductResponse> of(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getQuantity(), menuProduct.getProduct().getId());
    }

    public MenuProductResponse(long quantity, long productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getProductId() {
        return productId;
    }
}
