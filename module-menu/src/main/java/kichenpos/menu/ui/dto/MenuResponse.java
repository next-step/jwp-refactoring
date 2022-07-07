package kichenpos.menu.ui.dto;

import kichenpos.menu.domain.Menu;
import kichenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice().value();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProducts = menu.getMenuProducts();
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
