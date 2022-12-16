package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProducts {
    private final List<MenuProduct> menuProducts;

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<Long> toProductIds() {
        return menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
    }
}
