package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class TestMenuRequestFactory {
    public static MenuRequest toMenuRequest(Menu menu) {
        return new MenuRequest(
                menu.getName().toString(),
                menu.getPrice().value(),
                menu.getMenuGroupId(),
                menu.getMenuProducts()
                        .values()
                        .stream()
                        .map(m -> new MenuProductRequest(m.getProductId(), m.getQuantity().value()))
                        .collect(Collectors.toList()));
    }

    public static MenuRequest toMenuRequest(String name, int price, Long menuGroupId) {
        return toMenuRequest("", BigDecimal.valueOf(price), menuGroupId);
    }

    public static MenuRequest toMenuRequest(String name, BigDecimal price, Long menuGroupId) {
        return new MenuRequest("", price, menuGroupId, null);
    }

    public static MenuRequest toMenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

}
