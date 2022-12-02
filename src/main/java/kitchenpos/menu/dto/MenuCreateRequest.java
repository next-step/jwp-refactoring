package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    private List<MenuProduct> menuProducts;
    private Long menuGroupId;
    private BigDecimal price;

    public MenuCreateRequest(BigDecimal price) {
        this.price = price;
    }

    public Menu toMenu() {
        return new Menu();
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }

    public Long getMenuGroupId() {
        return this.menuGroupId;
    }

    public BigDecimal getPrice() {
        return this.price;
    }
}
