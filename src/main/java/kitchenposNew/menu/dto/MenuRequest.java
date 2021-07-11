package kitchenposNew.menu.dto;

import kitchenpos.domain.MenuProduct;
import kitchenposNew.menu.domain.Menu;
import kitchenposNew.wrap.Price;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    public String name;
    public BigDecimal price;
    public Long menuGroupId;
    public List<MenuProduct> menuProducts;

    protected MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu() {
        return new Menu(name, new Price(price), menuGroupId, menuProducts);
    }

    public Long getMenuGroupId() {
        return this.menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }
}
