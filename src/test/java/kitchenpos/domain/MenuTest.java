package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuTest {
    public static Menu 메뉴_생성(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();

        menu.setName(name);
        menu.setPrice(new BigDecimal(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }
}
