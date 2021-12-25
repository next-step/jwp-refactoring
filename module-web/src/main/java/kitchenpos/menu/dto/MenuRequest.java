package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;

public class MenuRequest {

    private final String name;
    private final Integer price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, Integer price, Long menuGroupId,
        List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity(MenuGroup menuGroup) {
        return Menu.of(name, price, menuGroup);
    }
}
