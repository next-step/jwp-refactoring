package kitchenpos.application.fixture;


import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class MenuProductFixtureFactory {

    private MenuProductFixtureFactory() {}

    public static MenuProduct create(Menu menu, Long productId, long quantity) {
        return MenuProduct.of(menu, productId, quantity);
    }

    public static MenuProduct create(Long id, Menu menu, Long productId, long quantity) {
        return MenuProduct.of(id, menu, productId, quantity);
    }
}
