package kitchenpos.menu.fixture;


import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixtureFactory {

    private MenuProductFixtureFactory() {}

    public static MenuProduct create(Long id, Menu menu, Long productId, long quantity) {
        return MenuProduct.of(id, menu, productId, quantity);
    }
}
