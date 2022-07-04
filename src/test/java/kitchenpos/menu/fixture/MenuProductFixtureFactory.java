package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.MenuProduct;
import org.springframework.test.util.ReflectionTestUtils;

public class MenuProductFixtureFactory {
    private MenuProductFixtureFactory() {
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct createMenuProduct(Long seq, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct(productId, quantity);
        ReflectionTestUtils.setField(menuProduct, "seq", seq);
        return menuProduct;
    }
}
