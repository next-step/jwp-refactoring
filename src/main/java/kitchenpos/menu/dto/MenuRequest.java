package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menuGroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private String name;
    private int price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
    }

    private MenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts){
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toMenu(MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }
}
