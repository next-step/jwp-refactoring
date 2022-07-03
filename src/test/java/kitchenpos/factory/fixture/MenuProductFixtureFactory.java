package kitchenpos.factory.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuProductFixtureFactory {
    public static MenuProduct createMenuProduct(Long seq, Menu menu, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenu(menu);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
