package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

public class MenuFixture {
    private MenuFixture() {
    }

    public static MenuRequest menuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public static Menu savedMenu(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }
}
