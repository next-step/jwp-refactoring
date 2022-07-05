package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.common.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

public class MenuFactory {
    public static Menu createMenu(Long id, String name, double price, Long menuGroupId,
                                  List<MenuProduct> menuProducts) {
        return new Menu(id, MenuName.from(name), Price.from(price), menuGroupId, MenuProducts.from(menuProducts));
    }

    public static MenuRequest createMenuRequest(String name, BigDecimal price, Long menuGroupId,
                                                List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }
}
