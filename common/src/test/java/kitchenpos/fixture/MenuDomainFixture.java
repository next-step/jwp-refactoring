package kitchenpos.fixture;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.dto.MenuRequest;

import java.math.BigDecimal;

import static kitchenpos.fixture.MenuGroupDomainFixture.일인_세트;

public class MenuDomainFixture {

    public static Menu 후라이드_치킨 = menu("후라이드 치킨", BigDecimal.valueOf(15000), 일인_세트);
    public static MenuRequest 후라이드_치킨_요청 = MenuRequest.of(후라이드_치킨.getName(),
            후라이드_치킨.getMenuPrice().getPrice(), 1L);

    public static Menu menu(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

}
