package kitchenpos.menu.util;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

import java.util.ArrayList;
import java.util.List;

public class MenuRequestBuilder {
    private String name;
    private int price;
    private Long menuGroupId;
    private final List<MenuProductRequest> menuProducts = new ArrayList<>();

    public MenuRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MenuRequestBuilder withPrice(int price) {
        this.price = price;
        return this;
    }

    public MenuRequestBuilder withGroupId(long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }

    public MenuRequestBuilder addMenuProduct(long productId, int quantity) {
        menuProducts.add(new MenuProductRequest(productId, quantity));
        return this;
    }

    public MenuRequest build() {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }
}
