package kitchenpos.domain.menu.fixture;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu_group.domain.MenuGroup;
import kitchenpos.domain.menu.domain.MenuProduct;
import kitchenpos.domain.menu.dto.MenuProductRequest;
import kitchenpos.domain.menu.dto.MenuRequest;

import java.math.BigDecimal;
import java.util.Arrays;

public class MenuFixture {

    public static Menu 메뉴(MenuGroup menuGroup, String name, BigDecimal price, MenuProduct menuProduct) {
        return new Menu(name, price, menuGroup, Arrays.asList(menuProduct));
    }

    public static MenuRequest 메뉴_요청(Long menuGroupId, String name, BigDecimal price, MenuProductRequest menuProductRequest) {
        return new MenuRequest(name, price, menuGroupId, Arrays.asList(menuProductRequest));
    }

    public static MenuRequest 메뉴_요청(MenuGroup menuGroup, String name, BigDecimal price, MenuProductRequest menuProductRequest) {
        return 메뉴_요청(menuGroup.getId(), name, price, menuProductRequest);
    }
}
