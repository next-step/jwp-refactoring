package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price = new Price();

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, name, new Price(price), menuGroupId);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(id, name, new Price(price), menuGroupId);
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            addMenuProduct(menuProduct);
        }
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        if (!menuProducts.contains(menuProduct)) {
            menuProduct.withMenu(this);
            menuProducts.addMenuProduct(menuProduct);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
