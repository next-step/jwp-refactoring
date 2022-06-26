package kitchenpos.factory;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixtureFactory {
    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }
}
