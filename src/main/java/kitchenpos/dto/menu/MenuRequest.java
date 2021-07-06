package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Price;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuRequest {
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, Price price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
