package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuTestFixture {
    public static  Menu 메뉴_생성(Long menuId, String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(menuId, name, new BigDecimal(price), menuGroup, menuProducts);
    }

    public static MenuProduct 메뉴_상품_생성(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }
}
