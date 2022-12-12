package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuTestFixture {

    public static Menu generateMenu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateMenuGroup(menuGroup);
        return Menu.of(id, name, price, menuGroup.getId(), menuProducts);
    }

    public static Menu generateMenu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateMenuGroup(menuGroup);
        return Menu.of(name, price, menuGroup.getId(), MenuProducts.from(menuProducts));
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

    private static void validateMenuGroup(MenuGroup menuGroup) {
        if(menuGroup == null) {
            throw new IllegalArgumentException(ErrorCode.메뉴_그룹은_비어있을_수_없음.getErrorMessage());
        }
    }
}
