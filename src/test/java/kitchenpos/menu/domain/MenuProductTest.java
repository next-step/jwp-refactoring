package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

public class MenuProductTest {

    public static MenuProduct 메뉴상품_생성(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct.Builder()
                .seq(seq)
                .menu(menu)
                .product(product)
                .quantity(quantity)
                .build();
    }
}