package kitchenpos.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class MenuProductFixture {

    public static MenuProduct 메뉴상품(Long seq, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct(productId, quantity);
        ReflectionTestUtils.setField(menuProduct, "seq", seq);
        return menuProduct;
    }

}
