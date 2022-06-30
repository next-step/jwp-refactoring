package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuName;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

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
