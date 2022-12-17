package kitchenpos.domain;

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
