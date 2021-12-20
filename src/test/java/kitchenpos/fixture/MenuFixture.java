package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menuGroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu 생성(Long id, String name, BigDecimal price, MenuGroup menuGroupId) {
        return new Menu(id,name,price,menuGroupId);
    }
}
