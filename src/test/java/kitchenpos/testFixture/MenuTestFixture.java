package kitchenpos.testFixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menuGroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;

public class MenuTestFixture {

    public static Menu 메뉴생성(Long id, String name, BigDecimal price, MenuGroup menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id,name,price,menuGroupId,menuProducts);
    }
}
