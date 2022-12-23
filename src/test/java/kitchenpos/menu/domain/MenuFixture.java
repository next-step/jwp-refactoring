package kitchenpos.menu.domain;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class MenuFixture {
    public static Menu 메뉴(Long id, String name, int price, MenuGroup menuGroup) {
        Menu menu = new Menu(name, price, menuGroup);
        ReflectionTestUtils.setField(menu, "id", id);
        return menu;
    }
}
