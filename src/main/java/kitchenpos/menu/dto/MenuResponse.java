package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuResponse {
    private String name;
    private long price;
    private MenuGroup menuGroup;
    private MenuProducts menuProducts;

    public MenuResponse(final String name, final long price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public MenuResponse setName(final String name) {
        this.name = name;
        return this;
    }

    public long getPrice() {
        return price;
    }

    public MenuResponse setPrice(final long price) {
        this.price = price;
        return this;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuResponse setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
        return this;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public MenuResponse setMenuProducts(final MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }
}
