package kitchenpos.Menu.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {
    public static Menu 메뉴(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(name, price, menuGroupId, menuProducts);
        ReflectionTestUtils.setField(menu, "id", id);
        return menu;
    }
}
