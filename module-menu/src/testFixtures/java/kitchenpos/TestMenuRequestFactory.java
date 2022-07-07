package kitchenpos;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

import java.math.BigDecimal;
import java.util.List;

public class TestMenuRequestFactory {
    public static MenuRequest toMenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

}
