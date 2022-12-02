package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProduct> menuProducts;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuProducts = menu.getMenuProducts();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }
}
