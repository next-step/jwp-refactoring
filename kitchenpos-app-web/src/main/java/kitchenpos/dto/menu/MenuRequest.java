package kitchenpos.dto.menu;


import kitchenpos.application.menu.Menu;
import kitchenpos.application.menu.MenuProducts;
import kitchenpos.application.menugroup.MenuGroup;
import kitchenpos.application.valueobject.Price;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {

    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu(MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, Price.of(price), menuGroup, menuProducts);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}