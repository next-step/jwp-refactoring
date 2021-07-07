package kitchenpos.domain.menuproduct;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Products;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuProducts {
    private List<MenuProduct> menuProducts;

    public static MenuProducts create(List<MenuProductCreate> menuProducts, Menu menu, Products products) {
        List<MenuProduct> results = menuProducts.stream()
                .map(item -> new MenuProduct(menu, products.findById(item.getProductId()), item.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(results);
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public Price sumAmount() {
        Price amount = menuProducts.stream()
                .map(item -> item.getAmount())
                .reduce(new Price(0), (before, appender) -> before.plus(appender));

        return amount;
    }
}
