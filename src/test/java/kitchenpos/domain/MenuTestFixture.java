package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

public class MenuTestFixture {

    public static Menu generateMenu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroup, menuProducts);
    }

    public static Menu generateMenu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(null, name, price, menuGroup, menuProducts);
    }

    public static MenuRequest generateMenuRequest(Name name, Price price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name.value(), price.value(), menuGroupId, menuProductRequests);
    }

    public static MenuRequest generateMenuRequest(Name name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name.value(), price, menuGroupId, menuProductRequests);
    }

    public static MenuRequest generateMenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }
}
