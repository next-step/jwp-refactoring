package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

public class MenuFactory {
    public static MenuRequest createMenuRequest(String name, BigDecimal price, Long menuGroupId,
                                                List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }
}
