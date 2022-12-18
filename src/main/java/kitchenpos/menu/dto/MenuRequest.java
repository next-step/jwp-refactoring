package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private String name;
    private int price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuRequest() {

    }

    public MenuRequest(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu() {
        return new Menu(name, new BigDecimal(price), menuGroupId, menuProducts);
    }

    public static MenuRequest of(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPrice().intValue(), menu.getMenuGroupId(), menu.getMenuProducts());
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
