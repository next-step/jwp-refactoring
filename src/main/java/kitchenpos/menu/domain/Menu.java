package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    private Long menuGroupId;
    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, long price, Long menuGroupId) {
        this.name = name;
        this.price = Price.valueOf(price);
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        menuProducts.forEach(this::addMenuProduct);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
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
