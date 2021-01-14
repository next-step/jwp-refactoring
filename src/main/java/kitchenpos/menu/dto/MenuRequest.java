package kitchenpos.menu.dto;

import kitchenpos.infra.Money;

import java.util.List;

public class MenuRequest {
    private final String name;
    private final long price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, long price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return Money.price(price);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public boolean isEmptyProducts() {
        return menuProducts.isEmpty();
    }
}
