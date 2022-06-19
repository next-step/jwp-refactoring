package kitchenpos.application.fixture;


import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuProductFixtureFactory {

    private MenuProductFixtureFactory() {}

    public static MenuProduct create(Menu menu, Product product, long quantity) {
        return MenuProduct.of(menu, product, quantity);
    }

    public static MenuProduct create(Long id, Menu menu, Product product, long quantity) {
        return MenuProduct.of(id, menu, product, quantity);
    }
}
