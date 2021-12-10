package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

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
