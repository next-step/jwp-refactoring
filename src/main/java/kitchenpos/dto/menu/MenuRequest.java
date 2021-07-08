package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Price;

import java.util.Collections;
import java.util.List;

public class MenuRequest {
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProductRequests;

    public MenuRequest(String name, Price price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return Collections.unmodifiableList(menuProductRequests);
    }
}
