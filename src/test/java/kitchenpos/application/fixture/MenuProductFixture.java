package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct createMenuProduct(final Long menuId, final Long productId,
        final Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
