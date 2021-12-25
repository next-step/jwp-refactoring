package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;
    
    private MenuRequest() {
    }

    private MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }
    
    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }
    
    public static MenuRequest from(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts());
    }
    
    public Menu toMenu(MenuGroup menuGroup) {
        return Menu.of(name, price, menuGroup, menuProducts);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setName(String name) {
        this.name = name;
    }

}
