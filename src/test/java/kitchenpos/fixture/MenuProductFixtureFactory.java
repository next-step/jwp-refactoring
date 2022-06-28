package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixtureFactory {
    private MenuProductFixtureFactory() {
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
