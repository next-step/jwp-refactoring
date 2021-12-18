package kitchenpos.domain;

import kitchenpos.menu.domain.MenuProduct;

import static kitchenpos.domain.MenuTest.통새우와퍼_세트;
import static kitchenpos.domain.ProductTest.콜라_상품;
import static kitchenpos.domain.ProductTest.통새우와퍼_상품;

public class MenuProductTest {
    public static final MenuProduct 통새우와퍼 = new MenuProduct(1L, 통새우와퍼_세트, 통새우와퍼_상품, 1);
    public static final MenuProduct 콜라 = new MenuProduct(2L, 통새우와퍼_세트, 콜라_상품, 1);
}
