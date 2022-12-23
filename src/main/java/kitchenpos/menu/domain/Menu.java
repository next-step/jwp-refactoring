package kitchenpos.menu.domain;

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
    private MenuPrice price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(Long id, String name, MenuPrice price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, MenuPrice price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, MenuPrice price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        menuProducts.setMenu(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public MenuPrice getPrice() {
        return price;
    }

    public BigDecimal getMenuPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts.getList();
    }
}
