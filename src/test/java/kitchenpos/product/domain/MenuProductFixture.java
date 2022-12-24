package kitchenpos.product.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.springframework.test.util.ReflectionTestUtils;

public class MenuProductFixture {

    public static MenuProduct 메뉴상품(Long seq, Long menuId, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct(menuId, product, quantity);
        ReflectionTestUtils.setField(menuProduct, "seq", seq);
        return menuProduct;
    }

}
