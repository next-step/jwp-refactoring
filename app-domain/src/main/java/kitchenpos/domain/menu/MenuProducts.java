package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;

import static java.util.stream.Collectors.*;

import java.util.List;


public class MenuProducts {
    private final List<MenuProduct> menuProducts;

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<Product> getProducts() {
        return menuProducts.stream()
                .map(MenuProduct::getProduct)
                .collect(toList());
    }
}
