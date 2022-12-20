package kitchenpos.Menu.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class MenuFixture {
    public static Menu 메뉴(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        Menu menu = new Menu(name, price, menuGroup);
        ReflectionTestUtils.setField(menu, "id", id);
        return menu;
    }
}
