package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class TestMenuProductFactory {
    public static MenuProduct create(Menu menu, Product product, long quantity) {
        return create(null, menu, product, quantity);
    }

    public static MenuProduct create(Long seq, Menu menu, Product product, long quantity) {
        return create(seq, menu.getId(), product.getId(), quantity);
    }

    public static MenuProduct create(Long menuId, Long productId, long quantity) {
        return create(null, menuId, productId, quantity);
    }

    public static MenuProduct create(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }
}
