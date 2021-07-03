package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;

public class Menu {
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private MenuProducts menuProducts;

    public Menu() {}

    public Menu(Long id, String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.menuGroupId = menuGroupId;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = Price.of(price);
    }

    public BigDecimal getPrice() {
        return price.toBigDecimal();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = MenuProducts.of(menuProducts);
    }

}
