package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuTest {
    public static Menu 메뉴_생성(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, new BigDecimal(price), menuGroupId, menuProducts);
    }
}
