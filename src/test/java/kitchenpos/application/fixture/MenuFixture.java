package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    private MenuFixture() {
    }

    public static Menu 메뉴생성(Long id, String name, Integer price, Long menuGroupId,
        MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Collections.singletonList(menuProduct));
        return menu;
    }

}
