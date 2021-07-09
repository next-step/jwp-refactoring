package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;

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
