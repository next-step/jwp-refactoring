package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

public class MenuProductFactory {

    public static MenuProduct create(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }
}
