package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menuGroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu 생성(String name, BigDecimal price, MenuGroup menuGroupId) {
        return new Menu(name,price,menuGroupId);
    }

    public static Menu 샘플(){
        MenuGroup 치킨류 = MenuGroupFixture.생성(1L, "치킨");
        return MenuFixture.생성("후라이드두마리세트", new BigDecimal("10000"), 치킨류);
    }
}
