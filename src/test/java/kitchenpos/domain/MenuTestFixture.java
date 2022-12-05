package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuTestFixture {

    public static Menu generateMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }
}
