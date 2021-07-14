package kitchenpos.dto.menu;


import kitchenpos.application.menu.MenuProduct;
import kitchenpos.application.menu.MenuProducts;
import kitchenpos.application.product.Product;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private long productId;
    private long quantity;

    public MenuProductResponse(Product product, long quantity) {

    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getProduct(),
                menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> ofList(MenuProducts menuProducts) {
        return menuProducts.getMenuProducts().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public MenuProductResponse(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
