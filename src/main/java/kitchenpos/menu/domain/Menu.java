package kitchenpos.menu.domain;

import kitchenpos.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts, MenuValidator menuValidator) {
        this.price = price;
        this.name = name;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        menuValidator.validate(price);
    }

    public static Menu create(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts, MenuValidator menuValidator) {
        return new Menu(name, Price.create(price), menuGroup, menuProducts, menuValidator);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.value();
    }

}
