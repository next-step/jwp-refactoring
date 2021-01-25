package kitchenpos.utils;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class TestFixture {
    // Product
    public static final Product 상품_후라이드 = new Product("후라이드", BigDecimal.valueOf(16000));
    public static final Product 상품_양념 = new Product("양념치킨", BigDecimal.valueOf(16000));

    // MenuProduct
    public static final MenuProduct 메뉴상품_후라이드 = new MenuProduct(상품_후라이드, 1);
    public static final MenuProduct 메뉴상품_양념 = new MenuProduct(상품_양념, 1);
}
