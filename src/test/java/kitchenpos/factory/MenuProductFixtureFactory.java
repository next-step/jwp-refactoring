package kitchenpos.factory;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixtureFactory {
    public static MenuProduct createMenuProduct(Long seq, Menu menu, Product product, int quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }
}
