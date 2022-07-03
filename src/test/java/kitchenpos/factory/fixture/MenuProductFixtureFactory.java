package kitchenpos.factory.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixtureFactory {
    public static MenuProduct createMenuProduct(Long seq, Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenu(menu);
        menuProduct.setProduct(product);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
