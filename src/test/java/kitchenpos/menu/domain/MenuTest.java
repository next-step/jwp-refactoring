package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuTest {
    public static Menu 메뉴_생성(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, new BigDecimal(price), menuGroupId, menuProducts);
    }

    public static Menu 메뉴_생성(Long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, new BigDecimal(price), menuGroupId, menuProducts);
    }
}
