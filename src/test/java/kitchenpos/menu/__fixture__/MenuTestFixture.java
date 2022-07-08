package kitchenpos.menu.__fixture__;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.request.MenuProductRequest;
import kitchenpos.menu.request.MenuRequest;

public class MenuTestFixture {
    public static MenuRequest 메뉴_요청_생성(final String name, final BigDecimal price, final MenuGroup menuGroup,
                                       final List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroup.getId(), menuProducts);
    }

    public static Menu 메뉴_생성(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup,
                             final List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }
}
