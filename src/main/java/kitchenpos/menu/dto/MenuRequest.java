package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

import java.util.List;

public class MenuRequest {
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    protected MenuRequest() {
    }

    public MenuRequest(String name, Long price, long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public static MenuRequest of(String name, Long price, long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }

    public Menu toMenu(MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroup, menuProducts);
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }
}
