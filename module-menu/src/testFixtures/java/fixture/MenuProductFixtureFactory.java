package fixture;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixtureFactory {
    public static MenuProduct createMenuProduct(Long productId, int quantity) {
        return MenuProduct.of(productId, quantity);
    }

    public static MenuProduct createMenuProduct(Long seq, Long productId, int quantity) {
        return MenuProduct.of(seq, productId, quantity);
    }
}
