package kitchenpos.domain.menu;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private MenuPrice price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    Menu(final String name, final BigDecimal price, final Long menuGroupId, final MenuProducts menuProducts) {
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, new MenuProducts(menuProducts));
    }

    public boolean isMoreExpensive(final BigDecimal price) {
        return this.getPrice().compareTo(price) > 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getList();
    }

    public List<Long> getProductIds() {
        return this.menuProducts.getProductIds();
    }
}
