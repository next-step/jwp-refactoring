package kitchenpos.domain;

import kitchenpos.menu.domain.MenuProduct;

import static kitchenpos.domain.MenuTest.치킨세트;
import static kitchenpos.domain.ProductTest.양념치킨_상품;
import static kitchenpos.domain.ProductTest.후라이드_상품;

public class MenuProductTest {
    public static final MenuProduct 후라이드 = new MenuProduct(1L, 치킨세트, 후라이드_상품, 1);
    public static final MenuProduct 양념치킨 = new MenuProduct(2L, 치킨세트, 양념치킨_상품, 1);
}
