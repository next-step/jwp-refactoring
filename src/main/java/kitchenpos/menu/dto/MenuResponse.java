package kitchenpos.menu.dto;

import java.math.*;
import java.util.*;

import kitchenpos.menu.domain.*;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    private MenuResponse(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup(), menu.getMenuProducts());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuResponse that = (MenuResponse)o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price)
            && Objects.equals(menuGroup, that.menuGroup) && Objects.equals(menuProducts,
            that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroup, menuProducts);
    }
}
