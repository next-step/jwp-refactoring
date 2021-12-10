package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menugroup.MenuGroup;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.fixture.MenuGroupDomainFixture.일인_세트;

public class MenuDomainFixture {

    public static Menu 후라이드_치킨 = menu("후라이드 치킨", BigDecimal.valueOf(15000), 일인_세트);

    public static Menu menu(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

}
