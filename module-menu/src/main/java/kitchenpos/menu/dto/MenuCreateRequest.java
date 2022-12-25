package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    private List<MenuProduct> menuProducts;
    private Long menuGroupId;
    private BigDecimal price;
    private String name;

    public MenuCreateRequest(List<MenuProduct> menuProducts, Long menuGroupId, BigDecimal price, String name) {
        this.menuProducts = menuProducts;
        this.menuGroupId = menuGroupId;
        this.price = price;
        this.name = name;
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

    public String getName() {
        return this.name;
    }
}
