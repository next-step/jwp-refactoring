package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;

import java.math.BigDecimal;
import java.util.Objects;

public class MenuResponse {

    private final Long id;
    private final BigDecimal price;
    private final String name;
    private final MenuGroup menuGroup;
    private final MenuProducts menuProducts;

    public MenuResponse(Long id, BigDecimal price, String name, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getPrice(), menu.getName(), menu.getMenuGroup(), menu.getMenuProducts());
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuResponse that = (MenuResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(price, that.price) && Objects.equals(name, that.name) && Objects.equals(menuGroup, that.menuGroup) && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, name, menuGroup, menuProducts);
    }
}
